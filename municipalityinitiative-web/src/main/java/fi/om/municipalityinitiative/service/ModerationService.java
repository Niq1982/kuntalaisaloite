package fi.om.municipalityinitiative.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
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
    public void accept(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment, Locale locale) {
        loginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        if (isDraftReview(initiative)) {
            acceptDraftReview(moderatorComment, locale, initiative);
        }
        else if (isFixStateReview(initiative)) {
            acceptFixStateReview(initiativeId, moderatorComment);
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

    private void acceptFixStateReview(Long initiativeId, String moderatorComment) {
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.OK);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_FIX);
    }

    private void acceptDraftReview(String moderatorComment, Locale locale, Initiative initiative) {
        Long initiativeId = initiative.getId();

        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);

        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiativeId);
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiativeId, locale);
        } else {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM);
        }
    }

    @Transactional(readOnly = false)
    public void reject(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment) {
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
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    private void markStateAsDraftAndSendEmails(Long initiativeId, String moderatorComment) {
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthors(OmLoginUserHolder loginUserHolder, Long initiativeId) {
        loginUserHolder.assertOmUser();
        return authorDao.findAuthors(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<MunicipalityEditDto> findMunicipalitiesForEdit(OmLoginUserHolder loginUserHolder) {
        loginUserHolder.assertOmUser();
        return municipalityDao.findMunicipalitiesForEdit();
    }

    @Transactional(readOnly = false)
    public void updateMunicipality(OmLoginUserHolder omLoginUserHolder, MunicipalityUIEditDto editDto) {
        omLoginUserHolder.assertOmUser();
        municipalityDao.updateMunicipality(editDto.getId(), editDto.getMunicipalityEmail(), Boolean.TRUE.equals(editDto.getActive()));
    }

    @Transactional(readOnly = false)
    public void sendInitiativeBackForFixing(OmLoginUserHolder omLoginUserHolder, Long initiativeId, String moderatorComment) {

        omLoginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!ManagementSettings.of(initiative).isAllowOmSendBackForFixing()) {
            throw new OperationNotAllowedException("Not allowed to send initiative back for fixing");
        }
        markFixStateAsFixAndSendEmails(initiativeId, moderatorComment);
    }

    @Transactional(readOnly = false)
    public void renewManagementHash(OmLoginUserHolder omLoginUserHolder, Long authorId) {
        omLoginUserHolder.assertOmUser();

        String newManagementHash = RandomHashGenerator.longHash();
        authorDao.updateManagementHash(authorId, newManagementHash);

        Set<Long> authorsInitiatives = authorDao.getAuthorsInitiatives(newManagementHash);
        // TODO: Multiple initiatives under one author is no more possible?
        Initiative initiative = initiativeDao.get(authorsInitiatives.iterator().next());
        emailService.sendManagementHashRenewed(initiative, newManagementHash, authorId);
    }
}
