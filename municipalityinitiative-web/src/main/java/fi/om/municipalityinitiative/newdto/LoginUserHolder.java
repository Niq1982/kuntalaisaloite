package fi.om.municipalityinitiative.newdto;

import fi.om.municipalityinitiative.newdto.user.LoginUser;
import fi.om.municipalityinitiative.service.AccessDeniedException;

public class LoginUserHolder {

    LoginUser loginUser;

    public LoginUserHolder(LoginUser loginUser) {
        if (loginUser == null)
            throw new RuntimeException("User was null");
        this.loginUser = loginUser;
    }

    public LoginUser getLoginUser() {
        return loginUser;
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
        return loginUser.hasRightToInitiative(initiativeId);
    }

    public Long getAuthorId() {
        return loginUser.getAuthorId();
    }

    public void assertOmUser() {
        if (loginUser.isNotOmUser()){
            throw new AccessDeniedException("No privileges");
        }
    }
}
