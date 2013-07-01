package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.exceptions.AccessDeniedException;

public class LoginUserHolder<E extends User> {

    E user;

    public LoginUserHolder(E user) {
        if (user == null)
            throw new RuntimeException("User was null");
        this.user = user;
    }

    public E getUser() {
        return user;
    }

    public void assertManagementRightsForInitiative(Long initiativeId) {
        if (hasNoManagementRightsForInitiative(initiativeId)) {
            throwAccessDeniedException(initiativeId);
        }
    }

    private static void throwAccessDeniedException(Long initiativeId) {
        throw new AccessDeniedException("No access for initiative with id: " + initiativeId);
    }

    private boolean hasNoManagementRightsForInitiative(Long initiativeId) {
        return !hasManagementRightsForInitiative(initiativeId);
    }

    public boolean hasManagementRightsForInitiative(Long initiativeId) {
        return user.hasRightToInitiative(initiativeId);
    }

    public NormalLoginUser getNormalLoginUser() {
        // TODO: Throw accessdenied or something
        return (NormalLoginUser) user;
    }

    public VerifiedUser getVerifiedUser() {
        // TODO: Throw some exception which redirects to vetuma.
        if (!isVerifiedUser()) {
            throw new AccessDeniedException("Not logged in");
        }
        return (VerifiedUser) user;
    }

    public boolean isVerifiedUser() {
        return user instanceof VerifiedUser;
    }

    public void assertOmUser() {
        if (user.isNotOmUser()){
            throw new AccessDeniedException("No privileges");
        }
    }

    public void assertViewRightsForInitiative(Long initiativeId) {
        if (!hasManagementRightsForInitiative(initiativeId) && user.isNotOmUser()) {
            throwAccessDeniedException(initiativeId);
        }
    }
}
