package fi.om.municipalityinitiative.service;

import java.util.Locale;

import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;
import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdao.MunicipalityDao;
import fi.om.municipalityinitiative.newdto.Author;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.ManagementSettings;
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

    @Transactional(readOnly = false)
    public void accept(Long initiativeId, String comment, Locale locale) {
        userService.requireOmUser();
        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);

        if (!ManagementSettings.of(initiative).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to accept initiative");
        }

        initiativeDao.updateModeratorComment(initiativeId, comment);
        if (initiative.getType().equals(InitiativeType.SINGLE)) {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.PUBLISHED);
            initiativeDao.markInitiativeAsSent(initiativeId);
            initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId); // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.ACCEPTED_BY_OM_AND_SENT, locale);
            emailService.sendSingleToMunicipality(initiative, municipalityDao.getMunicipalityEmail(initiative.getMunicipality().getId()), locale);
        }
        else {
            initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
            initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);  // Necessary because initiative is updated
            emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.ACCEPTED_BY_OM, locale);
        }

    }

    @Transactional(readOnly = false)
    public void reject(Long initiativeId, String comment, Locale locale) {
        userService.requireOmUser();
        if (!ManagementSettings.of(initiativeDao.getByIdWithOriginalAuthor(initiativeId)).isAllowOmAccept()) {
            throw new OperationNotAllowedException("Not allowed to reject initiative");

        }
        initiativeDao.updateModeratorComment(initiativeId, comment);
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.REJECTED_BY_OM, locale);
    }

    @Transactional(readOnly = true)
    public Author getAuthorInformation(Long initiativeId) {
        userService.requireOmUser();
        return initiativeDao.getByIdWithOriginalAuthor(initiativeId).getAuthor();
    }
}
