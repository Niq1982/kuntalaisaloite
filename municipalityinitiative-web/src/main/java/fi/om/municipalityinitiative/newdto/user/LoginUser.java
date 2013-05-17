package fi.om.municipalityinitiative.newdto.user;

import java.util.Set;

public abstract class LoginUser {

    public static LoginUser omUser() {
        return new OmLoginUser();
    }

    public static LoginUser normalUser(Long authorId, Set<Long> authorsInitiatives) {
        return new NormalLoginUser(authorId, authorsInitiatives);
    }

    public static LoginUser anonym() {
        return new AnonymLoginUser();
    }

    public abstract boolean isOmUser();

    public boolean isNotOmUser() {
        return !isOmUser();
    }

    public abstract boolean hasRightToInitiative(Long initiativeId);

    public abstract Long getAuthorId();

}
