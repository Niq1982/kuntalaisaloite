package fi.om.municipalityinitiative.dto.user;


import fi.om.municipalityinitiative.dto.service.Municipality;

public class MunicipalityLoginUser extends User {


    private final Long initiativeId;

    MunicipalityLoginUser(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean isVerifiedUser() {
        return false;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
       return false;
    }

    @Override
    public boolean hasParticipatedToInitiative(Long initiativeId) {
        return false;
    }

    @Override
    public boolean allowVerifiedParticipation(Long initiativeId, Municipality municipality) {
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean isMunicipalityUser(Long initiativeId) {
        return initiativeId.equals(this.initiativeId);
    }
}
