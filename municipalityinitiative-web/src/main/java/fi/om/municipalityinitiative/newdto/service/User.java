package fi.om.municipalityinitiative.newdto.service;

import java.util.Collections;
import java.util.Set;

public class User {

    private final boolean omUser;
    private Set<Long> authorsInitiatives;
    private Long authorId;

    private User(boolean omUser, Long authorId, Set<Long> authorsInitiatives) {
        this.omUser = omUser;
        this.authorsInitiatives = authorsInitiatives;
        this.authorId = authorId;
    }

    public static User omUser() {
        return new User(true, null, Collections.<Long>emptySet());
    }

    public static User normalUser(Long authorId, Set<Long> authorsInitiatives) {
        return new User(false, authorId, authorsInitiatives);
    }

    public boolean isOmUser() {
        return omUser;
    }

    public boolean isNotOmUser() {
        return !isOmUser();
    }

    public boolean hasRightToInitiative(Long initiativeId) {
        return authorsInitiatives.contains(initiativeId);
    }

    public Long getAuthorId() {
        return authorId;
    }
}
