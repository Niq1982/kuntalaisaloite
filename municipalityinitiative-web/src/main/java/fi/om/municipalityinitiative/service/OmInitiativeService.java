package fi.om.municipalityinitiative.service;

import java.util.Locale;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
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
        emailService.sendStatusEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), "mikko.lehtinen@solita.fi", EmailMessageType.ACCEPTED_BY_OM_AND_SENT, locale);
    }

    // TODO: IsAllowed
    public void reject(Long initiativeId, Locale locale) {
        userService.requireOmUser();
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
        emailService.sendStatusEmail(initiativeDao.getByIdWithOriginalAuthor(initiativeId), "mikko.lehtinen@solita.fi", EmailMessageType.REJECTED_BY_OM, locale);
    }
}
