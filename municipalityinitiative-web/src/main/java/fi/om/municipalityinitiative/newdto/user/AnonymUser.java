package fi.om.municipalityinitiative.newdto.user;

public class AnonymUser extends User {


    AnonymUser() { }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
        return false;
    }

    @Override
    public Long getAuthorId() {
        throw new IllegalStateException("Anonym user is no author");
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
