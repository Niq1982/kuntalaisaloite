package fi.om.municipalityinitiative.newdto.user;

public class AnonymLoginUser extends LoginUser {


    AnonymLoginUser() { }

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
}
