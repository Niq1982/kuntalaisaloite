package fi.om.municipalityinitiative.dto.user;

public class OmLoginUser extends User {

    private final String name;

    OmLoginUser(String name) {
        this.name = name;
    }

    @Override
    public boolean isOmUser() {
        return true;
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
    public boolean isLoggedIn() {
        return true;
    }

    public String getName() {
        return name;
    }
}
