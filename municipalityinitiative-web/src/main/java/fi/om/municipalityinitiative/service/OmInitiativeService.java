package fi.om.municipalityinitiative.service;

import java.util.Locale;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.util.InitiativeState;

import javax.annotation.Resource;

public class OmInitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    UserService userService;
    
    @Resource
    EmailService emailService;

    // TODO: IsAllowed
    public void accept(Long initiativeId, Locale locale) {
        userService.requireOmUser();
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.ACCEPTED_BY_OM_AND_SENT, locale);
    }

    // TODO: IsAllowed
    public void reject(Long initiativeId, Locale locale) {
        userService.requireOmUser();
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);

        Initiative initiative = initiativeDao.getByIdWithOriginalAuthor(initiativeId);
        emailService.sendStatusEmail(initiative, initiative.getAuthor().getContactInfo().getEmail(), EmailMessageType.REJECTED_BY_OM, locale);
    }
}
