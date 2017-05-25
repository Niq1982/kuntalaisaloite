package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public abstract class User implements Serializable {

    public static OmLoginUser omUser(String name) {
        return new OmLoginUser(name);
    }

    public static MunicipalityLoginUser municipalityLoginUser(Long initiativeId) {
        return new MunicipalityLoginUser(initiativeId);
    }

    public static NormalLoginUser normalUser(NormalAuthorId authorId, Set<Long> authorsInitiatives) {
        return new NormalLoginUser(authorId, authorsInitiatives);
    }

    public static VerifiedUser verifiedUser(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, Set<Long> initiatives, Set<Long> initiativesWithParticipation, Optional<Municipality> homeMunicipality, int age) {
        return new VerifiedUser(verifiedUserId, hash, contactInfo, initiatives, initiativesWithParticipation, homeMunicipality, age);
    }

    public abstract boolean tooYoungForVerifiedParticipation();

    public static User anonym() {
        return new AnonymUser();
    }

    public abstract boolean isOmUser();

    public abstract boolean isVerifiedUser();

    public boolean isNotOmUser() {
        return !isOmUser();
    }

    public abstract boolean hasRightToInitiative(Long initiativeId);

    public abstract boolean hasParticipatedToInitiative(Long initiativeId);
    
    public abstract boolean municipalityOkForVerifiedParticipation(Long initiativeId, Municipality municipality);

    public abstract boolean isLoggedIn();

    public abstract boolean isMunicipalityUser(Long initiativeId);

    public boolean isNotMunicipalityLoginUser(Long initiativeId) {
        return !isMunicipalityUser(initiativeId);
    }

}
