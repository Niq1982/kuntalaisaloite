package fi.om.municipalityinitiative.service.ui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.NormalAuthor;
import fi.om.municipalityinitiative.dto.VerifiedAuthor;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.*;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.VerifiedUser;
import fi.om.municipalityinitiative.exceptions.InvalidVideoUrlException;
import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.VideoService;
import fi.om.municipalityinitiative.service.YouthInitiativeWebServiceNotifier;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.*;
import fi.om.municipalityinitiative.util.hash.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class InitiativeManagementService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    LocationDao locationDao;

    @Resource
    EmailService emailService;

    @Resource
    UserDao userDao;

    @Resource
    ParticipantDao participantDao;

    @Resource
    private ReviewHistoryDao reviewHistoryDao;

    @Resource
    private YouthInitiativeWebServiceNotifier youthInitiativeWebServiceNotifier;

    @Resource
    private MunicipalityUserDao municipalityUserDao;

    @Resource
    private VideoService videoService;

    @Transactional(readOnly = true)
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Edit initiative", ManagementSettings.of(initiative).isAllowEdit());

        ContactInfo contactInfo;

        if (loginUserHolder.isVerifiedUser()) {
            contactInfo = authorDao.getVerifiedAuthor(initiativeId, loginUserHolder.getVerifiedUser().getAuthorId()).getContactInfo();
        }
        else {
            contactInfo = authorDao.getNormalAuthor(loginUserHolder.getNormalLoginUser().getAuthorId()).getContactInfo();
        }

        return InitiativeDraftUIEditDto.parse(initiative,contactInfo,locationDao.getLocations(initiativeId));
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeDraftUIEditDto editDto, Locale locale) throws MalformedURLException, InvalidVideoUrlException {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Edit initiative", ManagementSettings.of(initiative).isAllowEdit());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
        initiativeDao.addVideoUrl(videoService.convertVideoUrl(editDto.getVideoUrl()), initiativeId);

        locationDao.removeLocations(initiativeId);
        locationDao.setLocations(initiativeId, editDto.getLocations());

        if (loginUserHolder.isVerifiedUser()) {
            String hash = loginUserHolder.getVerifiedUser().getHash();
            userDao.updateUserInformation(hash, editDto.getContactInfo());
            participantDao.updateVerifiedParticipantShowName(initiativeId, hash, editDto.getContactInfo().isShowName());
            initiativeDao.denormalizeParticipantCountForVerifiedInitiative(initiativeId);

            // This is a little strange :)
            // When verified user starts creating a initiative, we'll send the first email after he/she updates
            // the initiative for the first time. But it's better to send it here rather than immediately when he
            // starts creating it from prepare-page.
            if (Strings.isNullOrEmpty(initiative.getName())) {
                emailService.sendVeritiedInitiativeManagementLink(initiativeId, locale);
            }

        }
        else {
            authorDao.updateAuthorInformation(loginUserHolder.getNormalLoginUser().getAuthorId(), editDto.getContactInfo());
            initiativeDao.denormalizeParticipantCountForNormalInitiative(initiativeId);
        }


    }

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, LoginUserHolder loginUserHolder) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Update initiative", ManagementSettings.of(initiative).isAllowUpdate());

        ContactInfo contactInfo;
        if (initiative.getType().isNotVerifiable()) {
            contactInfo = authorDao.getNormalAuthor(loginUserHolder.getNormalLoginUser().getAuthorId()).getContactInfo();
        }
        else {
            contactInfo = authorDao.getVerifiedAuthor(initiativeId, loginUserHolder.getVerifiedUser().getAuthorId()).getContactInfo();
        }

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(contactInfo);
        updateDto.setExtraInfo(initiative.getExtraInfo());
        updateDto.setExternalParticipantCount(initiative.getExternalParticipantCount());
        updateDto.setLocations(locationDao.getLocations(initiativeId));
        if (initiative.getVideoUrl().isPresent()) {
            updateDto.setVideoUrl(initiative.getVideoUrl().getValue());
        }
        return updateDto;
    }

    @Transactional(readOnly = true)
    // TODO: Tests?
    public Author getAuthorInformation(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!loginUserHolder.isVerifiedUser()) {
            for (NormalAuthor author : authorDao.findNormalAuthors(initiativeId)) {
                if (author.getId().equals(loginUserHolder.getNormalLoginUser().getAuthorId())) {
                    return author;
                }
            }
        }
        else {
            VerifiedAuthor author = new VerifiedAuthor();
            VerifiedUser dbVerifiedUser = userDao.getVerifiedUser(loginUserHolder.getVerifiedUser().getHash()).get();
            author.setContactInfo(dbVerifiedUser.getContactInfo());
            author.setMunicipality(Maybe.of(initiative.getMunicipality()));
            author.setId(null); // FIXME: Omg
            return author;
        }
        throw new NotFoundException("Author", initiativeId);
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeUIUpdateDto updateDto) throws MalformedURLException, InvalidVideoUrlException {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Update initiative", ManagementSettings.of(initiative).isAllowUpdate());

        initiativeDao.updateExtraInfo(initiativeId, updateDto.getExtraInfo(), updateDto.getExternalParticipantCount(), videoService.convertVideoUrl(updateDto.getVideoUrl()));

        locationDao.removeLocations(initiativeId);
        locationDao.setLocations(initiativeId, updateDto.getLocations());

        if (initiative.getType().isNotVerifiable()) {
            authorDao.updateAuthorInformation(loginUserHolder.getNormalLoginUser().getAuthorId(), updateDto.getContactInfo());
            initiativeDao.denormalizeParticipantCountForNormalInitiative(initiativeId);
        }
        else {
            String hash = loginUserHolder.getVerifiedUser().getHash();
            userDao.updateUserInformation(hash, updateDto.getContactInfo());
            participantDao.updateVerifiedParticipantShowName(initiativeId, hash, updateDto.getContactInfo().isShowName());
            initiativeDao.denormalizeParticipantCountForVerifiedInitiative(initiativeId);
        }
    }

    @Transactional(readOnly = false)
    public void sendReviewAndStraightToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Send review", ManagementSettings.of(initiative).isAllowSendToReview());

        if (initiative.getType().isVerifiable()) {
            throw new OperationNotAllowedException("Verifiable initiative cannot be sent straight to municipality");
        }

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        reviewHistoryDao.addReview(initiativeId, InitiativeSnapshotCreator.create(initiative));

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    @Transactional(readOnly = false)
    public void sendReviewWithUndefinedType(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Send review", ManagementSettings.of(initiative).isAllowSendToReview());
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        reviewHistoryDao.addReview(initiativeId, InitiativeSnapshotCreator.create(initiative));
        if (initiative.getType().isNotVerifiable()) {
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.UNDEFINED);
        }

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    @Transactional(readOnly = false)
    public void sendFixToReview(Long initiativeId, LoginUserHolder requiredLoginUserHolder) {
        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Send fix to review", ManagementSettings.of(initiative).isAllowSendFixToReview());
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.REVIEW);
        reviewHistoryDao.addReview(initiativeId, InitiativeSnapshotCreator.create(initiative));

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_FIX_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    @Transactional(readOnly = false)
    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);
        assertAllowance("Publish initiative", ManagementSettings.of(initiative).isAllowPublish());

        if (initiative.getType() == InitiativeType.UNDEFINED) { // Not verifiable initiative
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
        }

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.PUBLISHED_COLLECTING);
        if (initiative.getYouthInitiativeId().isPresent()) {
            youthInitiativeWebServiceNotifier.informInitiativePublished(initiative);
        }
    }

    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, LoginUserHolder requiredLoginUserHolder, String sentComment, Locale locale) {

        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);

        Initiative initiative = initiativeDao.get(initiativeId);

        assertAllowance("Send to municipality", ManagementSettings.of(initiative).isAllowSendToMunicipality());

        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        municipalityUserDao.assignMunicipalityUser(initiativeId, RandomHashGenerator.longHash());

        if (!initiative.getType().isCollaborative()) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
            emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_MUNICIPALITY);
            emailService.sendSingleToMunicipality(initiativeId, locale);
        }
        else {
            emailService.sendCollaborativeToAuthors(initiativeId);
            emailService.sendCollaborativeToMunicipality(initiativeId, locale);
            emailService.sendCollaborativeToMunicipalityToFollowers(initiativeId);
        }

        if (initiative.getYouthInitiativeId().isPresent()) {
            youthInitiativeWebServiceNotifier.informInitiativeSentToMunicipality(initiative);
        }


    }

    @Transactional(readOnly = true)
    public InitiativeViewInfo getMunicipalityInitiative(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        return InitiativeViewInfo.parse(initiativeDao.get(initiativeId));
    }

    @Transactional(readOnly = true)
    public List<InitiativeListInfo> findOwnInitiatives(LoginUserHolder loginUserHolder) {
        VerifiedUserId authorId;
        try {
            authorId = loginUserHolder.getVerifiedUser().getAuthorId();
        } catch (IllegalArgumentException e) {
            return Lists.newArrayList();
            // FIXME: getVerifiedUser().getAuthorId() should not throw exceptions, fix architecture
        }

        return initiativeDao.findInitiatives(authorId);
    }
}
