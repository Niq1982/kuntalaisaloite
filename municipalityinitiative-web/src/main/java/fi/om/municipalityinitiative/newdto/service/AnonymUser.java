package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.user.LoginUser;

public class AnonymUser extends LoginUser {
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
        throw new IllegalStateException("Anonymous user, not author");
    }
}
