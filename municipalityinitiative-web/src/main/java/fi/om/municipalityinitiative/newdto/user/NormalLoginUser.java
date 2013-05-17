package fi.om.municipalityinitiative.newdto.user;

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
    public Long getAuthorId() {
        return authorId;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

}
