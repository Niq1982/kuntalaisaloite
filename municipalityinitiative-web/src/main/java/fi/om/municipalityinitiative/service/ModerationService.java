package fi.om.municipalityinitiative.service;

import java.util.List;
import java.util.Locale;

import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.LoginUserHolder;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class ModerationService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    EmailService emailService;

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    AuthorDao authorDao;

    @Transactional(readOnly = false)
    public void accept(LoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment, Locale locale) {
        loginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        if (isDraftReview(initiative)) {
            acceptDraftReview(moderatorComment, locale, initiative);
        }
        else if (isFixStateReview(initiative)) {
            acceptFixStateReview(initiativeId, moderatorComment, initiative);
        }
        else {
            throw new IllegalStateException("Unable to accept initiative with state " + initiative.getState() + " and fixState " + initiative.getFixState());
        }
    }

    private static boolean isFixStateReview(Initiative initiative) {
        return initiative.getFixState() == FixState.REVIEW;
    }

    private static boolean isDraftReview(Initiative initiative) {
        return initiative.getState() == InitiativeState.REVIEW;
    }

    private void acceptFixStateReview(Long initiativeId, String moderatorComment, Initiative initiative) {
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.OK);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);
        initiative = initiativeDao.get(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.ACCEPTED_BY_OM);
    }

    private void acceptDraftReview(String moderatorComment, Locale locale, Initiative initiative) {
        Long initiativeId = initiative.getId();

        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);

        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiativeId);
            initiative = initiativeDao.get(initiativeId); // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiative, authorDao.findAuthors(initiativeId), municipalityEmail, locale);
        } else {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
            initiative = initiativeDao.get(initiativeId);  // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.ACCEPTED_BY_OM);
        }
    }

    @Transactional(readOnly = false)
    public void reject(LoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment) {
        loginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to reject initiative");
        }
        if (isDraftReview(initiative)) {
            markStateAsDraftAndSendEmails(initiativeId, moderatorComment);
        }
        else if (isFixStateReview(initiative)) {
            markFixStateAsFixAndSendEmails(initiativeId, moderatorComment);
        } else {
            throw new IllegalStateException("Invalid state for rejecting, there's something wrong with the code");
        }
    }

    private void markFixStateAsFixAndSendEmails(Long initiativeId, String moderatorComment) {
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.FIX);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        Initiative initiative = initiativeDao.get(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), EmailMessageType.REJECTED_BY_OM);
    }

    private void markStateAsDraftAndSendEmails(Long initiativeId, String moderatorComment) {
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
        Initiative initiative = initiativeDao.get(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthors(LoginUserHolder loginUserHolder, Long initiativeId) {
        loginUserHolder.assertOmUser();
        return authorDao.findAuthors(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<MunicipalityEditDto> findMunicipalitiesForEdit(LoginUserHolder loginUserHolder) {
        loginUserHolder.assertOmUser();
        return municipalityDao.findMunicipalitiesForEdit();
    }

    @Transactional(readOnly = false)
    public void updateMunicipality(LoginUserHolder requiredOmLoginUserHolder, MunicipalityUIEditDto editDto) {
        requiredOmLoginUserHolder.assertOmUser();
        municipalityDao.updateMunicipality(editDto.getId(), editDto.getMunicipalityEmail(), Boolean.TRUE.equals(editDto.getActive()));
    }

    @Transactional(readOnly = false)
    public void sendInitiativeBackForFixing(LoginUserHolder requiredOmLoginUserHolder, Long initiativeId, String moderatorComment) {

        requiredOmLoginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!ManagementSettings.of(initiative).isAllowOmSendBackForFixing()) {
            throw new OperationNotAllowedException("Not allowed to send initiative back for fixing");
        }
        markFixStateAsFixAndSendEmails(initiativeId, moderatorComment);
    }
}
