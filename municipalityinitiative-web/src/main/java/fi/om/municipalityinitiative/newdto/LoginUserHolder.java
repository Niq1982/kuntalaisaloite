package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.User;
import fi.om.municipalityinitiative.service.AccessDeniedException;
import fi.om.municipalityinitiative.util.Maybe;

public class LoginUserHolder {

    Maybe<Initiative> initiative;
    User user;

    public LoginUserHolder(User user, Maybe<Initiative> initiative) {
        if (user == null)
            throw new RuntimeException("User was null");
        this.user = user;
        this.initiative = initiative;
    }

    public Maybe<Initiative> getInitiative() {
        return initiative;
    }

    public User getUser() {
        return user;
    }

    public void assertManagementRightsForInitiative(Long initiativeId) {
        if (hasNoManagementRightsForInitiative(initiativeId)) {
            throw new AccessDeniedException("No access for initiative with id: " + initiativeId);
        }
    }

    private boolean hasNoManagementRightsForInitiative(Long initiativeId) {
        return !hasManagementRightsForInitiative(initiativeId);
    }

    public boolean hasManagementRightsForInitiative(Long initiativeId) {
        return user.hasRightToInitiative(initiativeId);
    }

    public Long getAuthorId() {
        return user.getAuthorId();
    }
}
