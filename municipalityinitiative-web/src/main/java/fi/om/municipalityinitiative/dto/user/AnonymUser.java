package fi.om.municipalityinitiative.dto.user;

public class AnonymUser extends User {

    AnonymUser() { }

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
    public boolean isLoggedIn() {
        return false;
    }
}
