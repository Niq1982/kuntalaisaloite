package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.exceptions.NotFoundException;
import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.dto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.dto.ui.InitiativeUIUpdateDto;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.operations.InitiativeManagementServiceOperations;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Locale;

import static fi.om.municipalityinitiative.util.SecurityUtil.assertAllowance;

public class InitiativeManagementService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    EmailService emailService;

    @Resource
    InitiativeManagementServiceOperations operations;

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

    public void sendReviewAndStraightToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        operations.doSendReviewStraightToMunicipality(initiativeId, sentComment);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    public void sendReviewOnlyForAcceptance(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);

        operations.doSendReviewOnlyForAcceptance(initiativeId);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    public void sendFixToReview(Long initiativeId, LoginUserHolder requiredLoginUserHolder) {
        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        operations.toSendFixToReview(initiativeId);

        emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_FIX_TO_REVIEW);
        emailService.sendNotificationToModerator(initiativeId);
    }

    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        operations.doPublishAndStartCollecting(initiativeId);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.PUBLISHED_COLLECTING);
    }

    public void sendToMunicipality(Long initiativeId, LoginUserHolder requiredLoginUserHolder, String sentComment, Locale locale) {

        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);

        InitiativeType initiativeType = operations.doSendToMunicipality(initiativeId, sentComment);

        if (initiativeType.isCollaborative()) {
            emailService.sendCollaborativeToAuthors(initiativeId);
            emailService.sendCollaborativeToMunicipality(initiativeId, locale);
        } else {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.SENT_TO_MUNICIPALITY);
            emailService.sendSingleToMunicipality(initiativeId, locale);
        }
    }

}
