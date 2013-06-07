package fi.om.municipalityinitiative.dto.user;

import java.util.Set;

public abstract class User {

    public static OmLoginUser omUser(String name) {
        return new OmLoginUser(name);
    }

    public static NormalLoginUser normalUser(Long authorId, Set<Long> authorsInitiatives) {
        return new NormalLoginUser(authorId, authorsInitiatives);
    }

    public static User anonym() {
        return new AnonymUser();
    }

    public abstract boolean isOmUser();

    public boolean isNotOmUser() {
        return !isOmUser();
    }

    public abstract boolean hasRightToInitiative(Long initiativeId);

    public abstract Long getAuthorId();

    public abstract boolean isLoggedIn();

}
