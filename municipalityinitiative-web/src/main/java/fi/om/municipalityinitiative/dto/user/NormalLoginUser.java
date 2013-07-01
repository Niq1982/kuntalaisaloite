package fi.om.municipalityinitiative.dto.user;

import java.util.Set;

public class NormalLoginUser extends User {

    private Set<Long> authorsInitiatives;

    private Long authorId;

    NormalLoginUser(Long authorId, Set<Long> authorsInitiatives) {
        this.authorsInitiatives = authorsInitiatives;
        this.authorId = authorId;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
        return authorsInitiatives.contains(initiativeId);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean isVerifiedUser() {
        return false;
    }

    public Long getAuthorId() {
        return authorId;
    }

}
