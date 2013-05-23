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
    public boolean hasRightToInitiative(Long initiativeId) {
        return false;
    }

    @Override
    public Long getAuthorId() {
        throw new IllegalStateException("Om user should have no need for author-id");
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    public String getName() {
        return name;
    }
}
