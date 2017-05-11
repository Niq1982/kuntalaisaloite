package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;

import java.util.HashSet;

public class VerifiedUserDbDetails {

    private final VerifiedUserId verifiedUserId;
    private final String hash;
    private final ContactInfo contactInfo;
    private final HashSet<Long> initiativesWithManagementRight;
    private final HashSet<Long> initiativesWithParticipation;
    private final Maybe<Municipality> homeMunicipality;

    public VerifiedUserDbDetails(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, HashSet<Long> initiativesWithManagementRight, HashSet<Long> initiativesWithParticipation, Maybe<Municipality> homeMunicipality) {
        this.verifiedUserId = verifiedUserId;
        this.hash = hash;
        this.contactInfo = contactInfo;
        this.initiativesWithManagementRight = initiativesWithManagementRight;
        this.initiativesWithParticipation = initiativesWithParticipation;
        this.homeMunicipality = homeMunicipality;
    }

    public VerifiedUserId getVerifiedUserId() {
        return verifiedUserId;
    }

    public String getHash() {
        return hash;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public HashSet<Long> getInitiativesWithManagementRight() {
        return initiativesWithManagementRight;
    }

    public HashSet<Long> getInitiativesWithParticipation() {
        return initiativesWithParticipation;
    }

    public Maybe<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }
}
