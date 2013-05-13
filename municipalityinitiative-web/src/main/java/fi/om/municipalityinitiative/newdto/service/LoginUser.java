package fi.om.municipalityinitiative.newdto.service;

import java.util.Set;

public abstract class LoginUser {

    public static LoginUser omUser() {
        return new OmLoginUser();
    }

    public static LoginUser normalUser(Long authorId, Set<Long> authorsInitiatives) {
        return new NormalLoginUser(authorId, authorsInitiatives);
    }

    public abstract boolean isOmUser();

    public boolean isNotOmUser() {
        return !isOmUser();
    }

    public abstract boolean hasRightToInitiative(Long initiativeId);

    public abstract Long getAuthorId();

    static class NormalLoginUser extends LoginUser {

        private Set<Long> authorsInitiatives;

        private Long authorId;
        private NormalLoginUser(Long authorId, Set<Long> authorsInitiatives) {
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

    static class OmLoginUser extends LoginUser {

        @Override
        public boolean isOmUser() {
            return true;
        }

        @Override
        public boolean hasRightToInitiative(Long initiativeId) {
            return false;
        }

        @Override
        public Long getAuthorId() {
            throw new RuntimeException("Om user should have no need for author-id");
        }
    }
}
