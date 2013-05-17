package fi.om.municipalityinitiative.newdto.user;

public class OmLoginUser extends User {

    OmLoginUser() { }

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
}
