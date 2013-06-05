package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.Participant;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Locale;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class InitiativeManagementService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    EmailService emailService;

    @Transactional(readOnly = true)
    public InitiativeDraftUIEditDto getInitiativeDraftForEdit(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        return InitiativeDraftUIEditDto.parse(
                initiativeDao.get(initiativeId),
                authorDao.getAuthor(loginUserHolder.getAuthorId()).getContactInfo()
        );
    }

    private ManagementSettings getManagementSettings(Long initiativeId) {
        return ManagementSettings.of(initiativeDao.get(initiativeId));
    }

    @Transactional(readOnly = false)
    public void editInitiativeDraft(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeDraftUIEditDto editDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        assertAllowance("Edit initiative", getManagementSettings(initiativeId).isAllowEdit());
        initiativeDao.editInitiativeDraft(initiativeId, editDto);
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), editDto.getContactInfo());
    }

    @Transactional(readOnly = true)
    public InitiativeUIUpdateDto getInitiativeForUpdate(Long initiativeId, LoginUserHolder loginUserHolder) {

        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());

        Initiative initiative = initiativeDao.get(initiativeId);

        ContactInfo contactInfo = authorDao.getAuthor(loginUserHolder.getAuthorId()).getContactInfo();

        InitiativeUIUpdateDto updateDto = new InitiativeUIUpdateDto();
        updateDto.setContactInfo(contactInfo);
        updateDto.setExtraInfo(initiative.getExtraInfo());

        return updateDto;
    }

    @Transactional(readOnly = true)
    // TODO: Tests?
    public Author getAuthorInformation(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        for (Author author : authorDao.findAuthors(initiativeId)) {
            if (author.getId().equals(loginUserHolder.getAuthorId())) {
                return author;
            }
        }
        throw new NotFoundException("Author", initiativeId);
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeUIUpdateDto updateDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());

        initiativeDao.updateExtraInfo(initiativeId, updateDto.getExtraInfo());
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), updateDto.getContactInfo());
    }

    @Transactional(readOnly = false) // XXX: Test that emails are sent
    public void sendReviewAndStraightToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.updateSentComment(initiativeId, sentComment);
    }

    @Transactional(readOnly = false)
    public void sendReviewOnlyForAcceptance(Long initiativeId, LoginUserHolder loginUserHolder) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder);
    }

    private void markAsReviewAndSendEmail(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send review", getManagementSettings(initiativeId).isAllowSendToReview());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    @Transactional(readOnly = false)
    public void sendFixToReview(Long initiativeId, LoginUserHolder requiredLoginUserHolder) {
        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send fix to review", getManagementSettings(initiativeId).isAllowSendFixToReview());
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.REVIEW);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_FIX_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    @Transactional(readOnly = false)
    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
        // XXX: TEST
        emailService.sendStatusEmail(initiativeId, EmailMessageType.PUBLISHED_COLLECTING);
    }

    @Transactional(readOnly = false)
    void publishAndSendToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale){
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        // XXX: TEST
        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_MUNICIPALITY);
        emailService.sendSingleToMunicipality(initiativeId, locale);
    }

    @Transactional(readOnly = false)
    void sendCollaborativeToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send collaborative to municipality", getManagementSettings(initiativeId).isAllowSendToMunicipality());

        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        Initiative initiative = initiativeDao.get(initiativeId);
        // XXX: TEST
        emailService.sendCollaborativeToMunicipality(initiativeId, locale);
        emailService.sendCollaborativeToAuthors(initiativeId);
    }


    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, LoginUserHolder requiredLoginUserHolder, String sentComment, Locale locale) {

        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);

        if (initiative.getType().isCollaborative()) {
            sendCollaborativeToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        } else {
            publishAndSendToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        }
    }

}
