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
    private final Set<Long> initiatives;
    private Maybe<Municipality> homeMunicipality;
    private VerifiedUserId authorId;

    VerifiedUser(VerifiedUserId verifiedUserId, String hash, ContactInfo contactInfo, Set<Long> initiatives, Maybe<Municipality> homeMunicipality) {
        this.hash = hash;
        this.authorId = verifiedUserId;
        // This is needed after we've logged in and participating or creating an initiative.
        // Data must be updated always when updating something at the UI
        this.contactInfo = contactInfo;
        this.initiatives = initiatives;
        this.homeMunicipality = homeMunicipality;
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
        return initiatives.contains(initiativeId);
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

    public Set<Long> getInitiatives() {
        return initiatives;
    }

    public Maybe<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }

    public VerifiedUserId getAuthorId() {
        Assert.notNull(authorId);
        return authorId;
    }
}
