package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.service.ReviewHistoryRow;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.dto.user.LoginUserHolder;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.service.YouthInitiativeWebServiceNotifier;
import fi.om.municipalityinitiative.service.email.EmailMessageType;
import fi.om.municipalityinitiative.service.email.EmailService;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Locale;

public class ModerationService {

    @Resource
    EmailService emailService;

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    AuthorDao authorDao;

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    AttachmentDao attachmentDao;

    @Resource
    ReviewHistoryDao reviewHistoryDao;

    @Resource
    YouthInitiativeWebServiceNotifier youthInitiativeWebServiceNotifier;

    @Transactional(readOnly = false)
    public void accept(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment, Locale locale) {
        loginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        attachmentDao.acceptAttachments(initiativeId);
        reviewHistoryDao.addAccepted(initiativeId, moderatorComment);

        if (initiative.getState() == InitiativeState.REVIEW) {
            acceptInitiativeDraft(locale, initiative);
        }
        else if (initiative.getFixState() == FixState.REVIEW) {
            acceptInitiativeFix(initiativeId);
        }
        else {
            // We should never get here.
            throw new IllegalStateException("Unable to accept initiative + " + initiativeId + " with state " + initiative.getState() + " and fixState " + initiative.getFixState());
        }
    }

    private void acceptInitiativeDraft(Locale locale, Initiative initiative) {
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiative.getId(), InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiative.getId());
            emailService.sendStatusEmail(initiative.getId(), EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiative.getId(), locale);
            if (initiative.getYouthInitiativeId().isPresent()) {
                youthInitiativeWebServiceNotifier.informInitiativeSentToMunicipality(initiative);
            }
        } else {
            initiativeDao.updateInitiativeState(initiative.getId(), InitiativeState.ACCEPTED);
            emailService.sendStatusEmail(initiative.getId(), EmailMessageType.ACCEPTED_BY_OM);
        }
    }

    private void acceptInitiativeFix(Long initiativeId) {
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.OK);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_FIX);
    }

    @Transactional(readOnly = false)
    public void reject(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment) {
        loginUserHolder.assertOmUser();
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to reject initiative");
        }

        if (initiative.getState() == InitiativeState.REVIEW) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
        }
        else if (initiative.getFixState() == FixState.REVIEW) {
            initiativeDao.updateInitiativeFixState(initiativeId, FixState.FIX);
        } else {
            throw new IllegalStateException("Invalid state for rejecting, there's something wrong with the code");
        }
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        attachmentDao.rejectAttachments(initiativeId);
        reviewHistoryDao.addRejected(initiativeId, moderatorComment);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = true)
    public List<? extends Author> findAuthors(LoginUserHolder loginUserHolder, Long initiativeId) {
        loginUserHolder.assertOmUser();
        if (initiativeDao.isVerifiableInitiative(initiativeId)) {
            return authorDao.findVerifiedAuthors(initiativeId);
        }
        else {
            return authorDao.findNormalAuthors(initiativeId);
        }
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
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.FIX);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        reviewHistoryDao.addRejected(initiativeId, moderatorComment);
        attachmentDao.rejectAttachments(initiativeId);
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = false)
    public void renewManagementHash(OmLoginUserHolder omLoginUserHolder, Long authorId) {
        omLoginUserHolder.assertOmUser();

        String newManagementHash = RandomHashGenerator.longHash();
        authorDao.updateManagementHash(new NormalAuthorId(authorId), newManagementHash);

        // NOTE: Now actually normal author has only one initiative...
        for (Long initiativeId : authorDao.getAuthorsInitiatives(newManagementHash)) {
            emailService.sendManagementHashRenewed(initiativeId, newManagementHash, authorId);
        }
    }

    @Transactional(readOnly = true)
    public List<ReviewHistoryRow> findReviewHistory(OmLoginUserHolder omLoginUserHolder, Long initiativeId) {
        omLoginUserHolder.assertOmUser();
        return reviewHistoryDao.findReviewHistoriesAndCommentsOrderedByTime(initiativeId);
    }

    @Transactional(readOnly = false)
    public void addComment(OmLoginUserHolder requiredOmLoginUserHolder, Long initiativeId, String comment) {
        requiredOmLoginUserHolder.assertOmUser();
        reviewHistoryDao.addReviewComment(initiativeId, requiredOmLoginUserHolder.getUser().getName() + ": " + comment);
    }
}
