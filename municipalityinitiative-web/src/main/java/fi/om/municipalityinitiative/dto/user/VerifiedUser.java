package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.Set;

public class VerifiedUser extends User{

    private final String hash;
    private final ContactInfo contactInfo;
    private final Set<Long> initiativesWithManagementRight;
    private final Set<Long> initiativesWithParticipation;
    private final int age;
    private final Optional<Municipality> homeMunicipality;
    private final VerifiedUserId authorId;

    VerifiedUser(VerifiedUserId verifiedUserId,
                 String hash,
                 ContactInfo contactInfo,
                 Set<Long> initiativesWithManagementRight,
                 Set<Long> initiativesWithParticipation,
                 Optional<Municipality> homeMunicipality,
                 int age) {
        this.hash = hash;
        this.authorId = verifiedUserId;
        // This is needed after we've logged in and participating or creating an initiative.
        // Data must be updated always when updating something at the UI
        this.contactInfo = contactInfo;
        this.initiativesWithManagementRight = initiativesWithManagementRight;
        this.homeMunicipality = homeMunicipality;
        this.initiativesWithParticipation = initiativesWithParticipation;
        this.age = age;
    }

    @Override
    public boolean tooYoungForVerifiedParticipation() {
        return age < 15;
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
    public boolean municipalityOkForVerifiedParticipation(Long initiativeId, Municipality municipality){
        return !hasParticipatedToInitiative(initiativeId)
                && (homeMunicipality.isPresent() && homeMunicipality.get().getId().equals(municipality.getId())
                || !homeMunicipality.isPresent());
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean isMunicipalityUser(Long initiativeId) {
        return false;
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

    public Optional<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }

    public VerifiedUserId getAuthorId() {
        Assert.notNull(authorId);
        return authorId;
    }

    public int getAge() {
        return age;
    }

    public Set<Long> getInitiativesWithParticipation() {
        return initiativesWithParticipation;
    }

}
