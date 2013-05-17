package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dao.NotFoundException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdao.ParticipantDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.service.Participant;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.InitiativeDraftUIEditDto;
import fi.om.municipalityinitiative.newdto.ui.InitiativeUIUpdateDto;
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
    MunicipalityDao municipalityDao;

    @Resource
    EmailService emailService;

    @Resource
    ParticipantDao participantDao;

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
        // TODO: Hmm.. We still get the municipality information and stuff from participant table...
        throw new NotFoundException("Author", initiativeId);
    }

    @Transactional(readOnly = false)
    public void updateInitiative(Long initiativeId, LoginUserHolder loginUserHolder, InitiativeUIUpdateDto updateDto) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Update initiative", getManagementSettings(initiativeId).isAllowUpdate());

        initiativeDao.updateExtraInfo(initiativeId, updateDto.getExtraInfo());
        authorDao.updateAuthorInformation(loginUserHolder.getAuthorId(), updateDto.getContactInfo());
    }

    @Transactional(readOnly = false)
    public void sendReviewAndStraightToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder, locale);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.updateSentComment(initiativeId, sentComment);
    }

    @Transactional(readOnly = false)
    public void sendReviewOnlyForAcceptance(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        markAsReviewAndSendEmail(initiativeId, loginUserHolder, locale);
    }

    private void markAsReviewAndSendEmail(Long initiativeId, LoginUserHolder loginUserHolder, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send review", getManagementSettings(initiativeId).isAllowSendToReview());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.REVIEW);
        Initiative initiative = initiativeDao.get(initiativeId);

        String TEMPORARILY_REPLACING_OM_EMAIL = authorDao.getAuthorEmails(initiativeId).get(0);

        emailService.sendNotificationToModerator(initiative, authorDao.findAuthors(initiativeId), locale, TEMPORARILY_REPLACING_OM_EMAIL);
    }

    @Transactional(readOnly = false)
    public void sendFixToReview(Long initiativeId, LoginUserHolder requiredLoginUserHolder) {
        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send fix to review", getManagementSettings(initiativeId).isAllowSendFixToReview());
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.REVIEW);
    }

    @Transactional(readOnly = false)
    public void publishAndStartCollecting(Long initiativeId, LoginUserHolder loginUserHolder) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.COLLABORATIVE);
        Initiative initiative = initiativeDao.get(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail(initiative), EmailMessageType.PUBLISHED_COLLECTING);
    }

    private String municipalityEmail(Initiative initiative) {
        return municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
    }

    @Transactional(readOnly = false)
    void publishAndSendToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale){
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Publish initiative", getManagementSettings(initiativeId).isAllowPublish());

        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
        initiativeDao.updateInitiativeType(initiativeId, InitiativeType.SINGLE);
        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        Initiative initiative = initiativeDao.get(initiativeId);
        emailService.sendStatusEmail(initiative,authorDao.getAuthorEmails(initiativeId), municipalityEmail(initiative), EmailMessageType.SENT_TO_MUNICIPALITY);
        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);
        emailService.sendSingleToMunicipality(initiative, authorDao.findAuthors(initiativeId), municipalityEmail, locale);
    }

    @Transactional(readOnly = false)
    void sendCollaborativeToMunicipality(Long initiativeId, LoginUserHolder loginUserHolder, String sentComment, Locale locale) {
        loginUserHolder.assertManagementRightsForInitiative(initiativeId);
        assertAllowance("Send collaborative to municipality", getManagementSettings(initiativeId).isAllowSendToMunicipality());

        initiativeDao.markInitiativeAsSent(initiativeId);
        initiativeDao.updateSentComment(initiativeId, sentComment);
        Initiative initiative = initiativeDao.get(initiativeId);
        List<Participant> participants = participantDao.findAllParticipants(initiativeId);
        // TODO: String municipalityEmail = municipalityEmail(initiative);
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);
        emailService.sendCollaborativeToMunicipality(initiative, authorDao.findAuthors(initiativeId), participants, municipalityEmail, locale);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.SENT_TO_MUNICIPALITY);
    }


    @Transactional(readOnly = false)
    public void sendToMunicipality(Long initiativeId, LoginUserHolder requiredLoginUserHolder, String sentComment, Locale locale) {

        requiredLoginUserHolder.assertManagementRightsForInitiative(initiativeId);
        Initiative initiative = initiativeDao.get(initiativeId);

        if (initiative.getType().isCollectable()) {
            sendCollaborativeToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        } else {
            publishAndSendToMunicipality(initiativeId, requiredLoginUserHolder, sentComment, locale);
        }
    }

}
