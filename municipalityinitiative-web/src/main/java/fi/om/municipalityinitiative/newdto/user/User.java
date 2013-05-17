package fi.om.municipalityinitiative.newdto.user;

import java.util.Set;

public abstract class User {

    public static User omUser() {
        return new OmLoginUser();
    }

    public static User normalUser(Long authorId, Set<Long> authorsInitiatives) {
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

}
