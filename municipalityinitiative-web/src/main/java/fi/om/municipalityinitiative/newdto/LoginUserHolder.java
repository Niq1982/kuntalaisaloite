package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.service.AccessDeniedException;

public class LoginUserHolder {

    Initiative initiative;
    User user;

    public LoginUserHolder(User user, Initiative initiative) {
        this.user = user;
        this.initiative = initiative;
    }

    public Initiative getInitiative() {
        return initiative;
    }

    public User getUser() {
        return user;
    }

    public void requireManagementRightsForInitiative(Long initiativeId) {
        if (initiative.getId().equals(initiativeId)) {
            throw new AccessDeniedException("No access for initiative with id: " + initiativeId);
        }
    }
}
