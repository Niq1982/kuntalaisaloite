package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.newdao.InitiativeDao;
import fi.om.municipalityinitiative.util.InitiativeState;

import javax.annotation.Resource;

public class OmInitiativeService {

    @Resource
    InitiativeDao initiativeDao;

    @Resource
    UserService userService;

    // TODO: IsAllowed
    public void accept(Long initiativeId) {
        userService.requireOmUser();
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.ACCEPTED);
    }

    // TODO: IsAllowed
    public void reject(Long initiativeId) {
        userService.requireOmUser();
        initiativeDao.updateInitiativeState(initiativeId, InitiativeState.DRAFT);
    }
}
