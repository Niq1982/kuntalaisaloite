package fi.om.municipalityinitiative.service.ui;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.dto.user.OmLoginUserHolder;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
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

    @Transactional(readOnly = false)
    public void accept(OmLoginUserHolder loginUserHolder, Long initiativeId, String moderatorComment, Locale locale) {
        loginUserHolder.assertOmUser();
        AcceptResult result;
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        if (initiative.getState() == InitiativeState.REVIEW) {
            Long initiativeId1 = initiative.getId();

            initiativeDao.updateModeratorComment(initiativeId1, moderatorComment);
            if (initiative.getType().equals(InitiativeType.SINGLE)) {
                initiativeDao.updateInitiativeState(initiativeId1, InitiativeState.PUBLISHED);
                initiativeDao.markInitiativeAsSent(initiativeId1);
                result = AcceptResult.ACCEPTED_DRAFT_AND_SENT;
            } else {
                initiativeDao.updateInitiativeState(initiativeId1, InitiativeState.ACCEPTED);
                result = AcceptResult.ACCEPTED_DRAFT;
            }
        }
        else if (initiative.getFixState() == FixState.REVIEW) {
            initiativeDao.updateInitiativeFixState(initiativeId, FixState.OK);
            initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
            result = AcceptResult.ACCEPTED_FIX;
        }
        else {
            throw new IllegalStateException("Unable to accept initiative with state " + initiative.getState() + " and fixState " + initiative.getFixState());
        }
        AcceptResult acceptResult = result;

        if (acceptResult == AcceptResult.ACCEPTED_DRAFT_AND_SENT) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiativeId, locale);
        }
        else if (acceptResult == AcceptResult.ACCEPTED_DRAFT) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM);
        }
        else if (acceptResult == AcceptResult.ACCEPTED_FIX) {
            emailService.sendStatusEmail(initiativeId, EmailMessageType.ACCEPTED_BY_OM_FIX);
        }
        else {
            throw new IllegalStateException("Unable to accept initiative with id:"+ initiativeId);
        }
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
        emailService.sendStatusEmail(initiativeId, EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = true)
    public List<? extends Author> findAuthors(OmLoginUserHolder loginUserHolder, Long initiativeId) {
        loginUserHolder.assertOmUser();
        return authorDao.findNormalAuthors(initiativeId);
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

    public enum AcceptResult {
        ACCEPTED_FIX,
        ACCEPTED_DRAFT,
        ACCEPTED_DRAFT_AND_SENT
    }
}
