package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.util.Assert;

import java.util.Set;

public class VerifiedUser extends User{

    private final String hash;
    private final ContactInfo contactInfo;
    private final Set<Long> initiativesWithManagementRight;
    private final Set<Long> initiativesWithParticipation;
    private final Maybe<Municipality> homeMunicipality;
    private final VerifiedUserId authorId;
    private Boolean adult;

    VerifiedUser(VerifiedUserId verifiedUserId,
                 String hash,
                 ContactInfo contactInfo,
                 Set<Long> initiativesWithManagementRight,
                 Set<Long> initiativesWithParticipation,
                 Maybe<Municipality> homeMunicipality) {
        this.hash = hash;
        this.authorId = verifiedUserId;
        // This is needed after we've logged in and participating or creating an initiative.
        // Data must be updated always when updating something at the UI
        this.contactInfo = contactInfo;
        this.initiativesWithManagementRight = initiativesWithManagementRight;
        this.homeMunicipality = homeMunicipality;
        this.initiativesWithParticipation = initiativesWithParticipation;
        this.adult = null;
    }

    public VerifiedUser(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, Set<Long> initiativesWithManagementRight, Set<Long> initiativesWithParticipation, Maybe<Municipality> homeMunicipality, boolean adult) {
        this.hash = hash;
        this.authorId = verifiedUserId;
        // This is needed after we've logged in and participating or creating an initiative.
        // Data must be updated always when updating something at the UI
        this.contactInfo = contactInfo;
        this.initiativesWithManagementRight = initiativesWithManagementRight;
        this.homeMunicipality = homeMunicipality;
        this.initiativesWithParticipation = initiativesWithParticipation;
        this.adult = adult;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean isVerifiedUser() {
        return true;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
        return initiativesWithManagementRight.contains(initiativeId);
    }

    @Override
    public boolean hasParticipatedToInitiative(Long initiativeId) {
        return initiativesWithParticipation.contains(initiativeId);
    }
    
    @Override
    public boolean allowVerifiedParticipation(Long initiativeId, Municipality municipality){
        return !hasParticipatedToInitiative(initiativeId)
                && (homeMunicipality.isPresent() && homeMunicipality.getValue().getId().equals(municipality.getId())
                || homeMunicipality.isNotPresent());
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    public String getHash() {
        return hash;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public Set<Long> getInitiativesWithManagementRight() {
        return initiativesWithManagementRight;
    }

    public Maybe<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }

    public VerifiedUserId getAuthorId() {
        Assert.notNull(authorId);
        return authorId;
    }

    public Set<Long> getInitiativesWithParticipation() {
        return initiativesWithParticipation;
    }

    public boolean isAdult() {
        Assert.notNull(this.adult); // XXX: Temporary, fix architecture.
        return adult;
    }

    public void readAdultValue(VerifiedUser original) {
        this.adult = original.isAdult();
    }
}
