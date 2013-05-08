package fi.om.municipalityinitiative.newdto.service;

import java.util.Collections;
import java.util.Set;

public class User {

    private final boolean omUser;
    private Set<Long> authorsInitiatives;

    private User(boolean omUser, Set<Long> authorsInitiatives) {
        this.omUser = omUser;

        this.authorsInitiatives = authorsInitiatives;
    }

    public static User omUser() {
        return new User(true, Collections.<Long>emptySet());
    }

    public static User normalUser(Set<Long> authorsInitiatives) {
        return new User(false, authorsInitiatives);
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
}
