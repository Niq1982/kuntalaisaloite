package fi.om.municipalityinitiative.service.operations;

import fi.om.municipalityinitiative.dao.AuthorDao;
import fi.om.municipalityinitiative.dao.InitiativeDao;
import fi.om.municipalityinitiative.dao.MunicipalityDao;
import fi.om.municipalityinitiative.dto.Author;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.ManagementSettings;
import fi.om.municipalityinitiative.dto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.dto.ui.MunicipalityUIEditDto;
import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.RandomHashGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Set;

public class ModerationServiceOperations {

    @Resource
    private InitiativeDao initiativeDao;

    @Resource
    private AuthorDao authorDao;

    @Resource
    private MunicipalityDao municipalityDao;

    public ModerationServiceOperations() {
    }

    // This constructor is for unit-testing. Replace those tests with integration-tests later.
    public ModerationServiceOperations(InitiativeDao initiativeDao, AuthorDao authorDao) {
        this.initiativeDao = initiativeDao;
        this.authorDao = authorDao;
    }

    @Transactional(readOnly = false)
    public AcceptResult doAccept(Long initiativeId, String moderatorComment) {
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        if (initiative.getState() == InitiativeState.REVIEW) {
            return acceptDraftReview(moderatorComment, initiative);
        }
        else if (initiative.getFixState() == FixState.REVIEW) {
            acceptFixStateReview(initiativeId, moderatorComment);
            return AcceptResult.ACCEPTED_FIX;
        }
        else {
            throw new IllegalStateException("Unable to accept initiative with state " + initiative.getState() + " and fixState " + initiative.getFixState());
        }
    }

    @Transactional(readOnly = false)
    public void doSendInitiativeBackForFixing(Long initiativeId, String moderatorComment) {
        Initiative initiative = initiativeDao.get(initiativeId);
        if (!ManagementSettings.of(initiative).isAllowOmSendBackForFixing()) {
            throw new OperationNotAllowedException("Not allowed to send initiative back for fixing");
        }
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.FIX);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthors(Long initiativeId) {
        return authorDao.findNormalAuthors(initiativeId);
    }

    @Transactional(readOnly = true)
    public List<MunicipalityEditDto> findMunicipalitiesForEdit() {
        return municipalityDao.findMunicipalitiesForEdit();
    }

    @Transactional(readOnly = false)
    public void doUpdateMunicipality(MunicipalityUIEditDto editDto) {
        municipalityDao.updateMunicipality(editDto.getId(), editDto.getMunicipalityEmail(), Boolean.TRUE.equals(editDto.getActive()));
    }

    public enum AcceptResult {
        ACCEPTED_FIX,
        ACCEPTED_DRAFT,
        ACCEPTED_DRAFT_AND_SENT
    }

    private void acceptFixStateReview(Long initiativeId, String moderatorComment) {
        initiativeDao.updateInitiativeFixState(initiativeId, FixState.OK);
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
    }

    private AcceptResult acceptDraftReview(String moderatorComment, Initiative initiative) {
        Long initiativeId = initiative.getId();

        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiativeId);
            return AcceptResult.ACCEPTED_DRAFT_AND_SENT;
        } else {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
            return AcceptResult.ACCEPTED_DRAFT;
        }
    }

    @Transactional(readOnly = false)
    public void doReject(Long initiativeId, String moderatorComment) {
        Initiative initiative = initiativeDao.get(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to reject initiative");
        }

        if (initiative.getState() == InitiativeState.REVIEW) {
            initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
        }
        else if (initiative.getFixState() == FixState.REVIEW) {
            initiativeDao.updateInitiativeFixState(initiativeId, FixState.FIX);
            initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        } else {
            throw new IllegalStateException("Invalid state for rejecting, there's something wrong with the code");
        }
    }

    @Transactional(readOnly = false)
    public ManagementHashRenewData doRenewManagementHash(Long authorId) {
        ManagementHashRenewData managementHashRenewData = new ManagementHashRenewData();

        managementHashRenewData.newManagementHash = RandomHashGenerator.longHash();
        authorDao.updateManagementHash(authorId, managementHashRenewData.newManagementHash);

        Set<Long> authorsInitiatives = authorDao.getAuthorsInitiatives(managementHashRenewData.newManagementHash);
        // TODO: Multiple initiatives under one author is no more possible?
        managementHashRenewData.initiativeId = authorsInitiatives.iterator().next();
        return managementHashRenewData;
    }

    public static class ManagementHashRenewData {
        public String newManagementHash;
        public Long initiativeId;
    }
}
