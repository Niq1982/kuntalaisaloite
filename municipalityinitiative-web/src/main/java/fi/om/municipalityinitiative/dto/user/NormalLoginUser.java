package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;

import java.util.Set;

public class NormalLoginUser extends User {

    private Set<Long> authorsInitiatives;

    private NormalAuthorId authorId;

    NormalLoginUser(NormalAuthorId authorId, Set<Long> authorsInitiatives) {
        this.authorsInitiatives = authorsInitiatives;
        this.authorId = authorId;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
        return authorsInitiatives.contains(initiativeId);
    }

    @Override
    public boolean hasParticipatedToInitiative(Long initiativeId) {
        return false;
    }
    
    @Override
    public boolean municipalityOkForVerifiedParticipation(Long initiativeId, Municipality municipality){
        return false;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean isMunicipalityUser(Long initiativeId) {
        return false;
    }

    @Override
    public boolean tooYoungForVerifiedParticipation() {
        return false;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean isVerifiedUser() {
        return false;
    }

    public NormalAuthorId getAuthorId() {
        return authorId;
    }

}
