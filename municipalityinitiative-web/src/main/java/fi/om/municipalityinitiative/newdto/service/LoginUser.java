package fi.om.municipalityinitiative.newdto.service;

import java.util.Collections;
import java.util.Set;

public class LoginUser {

    private final boolean omUser;
    private Set<Long> authorsInitiatives;
    private Long authorId;

    private LoginUser(boolean omUser, Long authorId, Set<Long> authorsInitiatives) {
        this.omUser = omUser;
        this.authorsInitiatives = authorsInitiatives;
        this.authorId = authorId;
    }

    public static LoginUser omUser() {
        return new LoginUser(true, null, Collections.<Long>emptySet());
    }

    public static LoginUser normalUser(Long authorId, Set<Long> authorsInitiatives) {
        return new LoginUser(false, authorId, authorsInitiatives);
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
