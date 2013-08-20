package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.NormalAuthorId;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.Set;

public abstract class User {

    public static OmLoginUser omUser(String name) {
        return new OmLoginUser(name);
    }

    public static NormalLoginUser normalUser(NormalAuthorId authorId, Set<Long> authorsInitiatives) {
        return new NormalLoginUser(authorId, authorsInitiatives);
    }

    public static VerifiedUser verifiedUser(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, Set<Long> initiatives, Set<Long> initiativesWithParticipation, Maybe<Municipality> homeMunicipality) {
        return new VerifiedUser(verifiedUserId, hash, contactInfo, initiatives, initiativesWithParticipation, homeMunicipality);
    }

    public static VerifiedUser verifiedUser(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, Set<Long> initiatives, Set<Long> initiativesWithParticipation, Maybe<Municipality> homeMunicipality, boolean isAdult) {
        return new VerifiedUser(verifiedUserId, hash, contactInfo, initiatives, initiativesWithParticipation, homeMunicipality, isAdult);
    }

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
    
    public abstract boolean allowVerifiedParticipation(Long initiativeId, Municipality municipality);

    public abstract boolean isLoggedIn();

}
