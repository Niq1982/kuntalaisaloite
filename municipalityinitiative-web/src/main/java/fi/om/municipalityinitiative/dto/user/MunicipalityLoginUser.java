package fi.om.municipalityinitiative.dto.user;


import fi.om.municipalityinitiative.dto.service.Municipality;

public class MunicipalityLoginUser extends User {


    private final String name;

    MunicipalityLoginUser(String name) {
        this.name = name;
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
        return false;
    }
}
