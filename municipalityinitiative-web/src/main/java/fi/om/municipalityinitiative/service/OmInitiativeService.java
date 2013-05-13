package fi.om.municipalityinitiative.service;

import java.util.List;
import java.util.Locale;

import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.AuthorDao;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityEditDto;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class OmInitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    UserService userService;

    @Resource
    EmailService emailService;

    @Resource
    MunicipalityDao municipalityDao;

    @Resource
    AuthorDao authorDao;

    @Transactional(readOnly = false)
    public void accept(Long initiativeId, String moderatorComment, Locale locale) {
        userService.requireOmUser();
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        // TODO: String municipalityEmail = municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId());
        String municipalityEmail = authorDao.getAuthorEmails(initiativeId).get(0);


        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiativeId);
            initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId); // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.ACCEPTED_BY_OM_AND_SENT);
            emailService.sendSingleToMunicipality(initiative, municipalityEmail, locale);
        } else {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
            initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);  // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityEmail, EmailMessageType.ACCEPTED_BY_OM);
        }

    }

    @Transactional(readOnly = false)
    public void reject(Long initiativeId, String moderatorComment) {
        userService.requireOmUser();
        if (!ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId)).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to reject initiative");

        }
        initiativeDao.updateModeratorComment(initiativeId, moderatorComment);
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative, authorDao.getAuthorEmails(initiativeId), municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), EmailMessageType.REJECTED_BY_OM);
    }

    @Transactional(readOnly = true)
    public List<Author> findAuthors(Long initiativeId) {
        userService.requireOmUser();
        return authorDao.findAuthors(initiativeId);
    }

    public List<MunicipalityEditDto> findMunicipalitiesForEdit() {
        return municipalityDao.findMunicipalitiesForEdit();
    }
}
