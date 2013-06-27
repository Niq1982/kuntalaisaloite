package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;

import java.util.Set;

public class VerifiedUser extends User{

    private final String ssn;
    private final Long authorId;
    private final ContactInfo contactInfo;
    private final Set<Long> initiatives;

    public VerifiedUser(String ssn, Long authorId, ContactInfo contactInfo, Set<Long> initiatives) {
        this.ssn = ssn;
        this.authorId = authorId;
        this.contactInfo = contactInfo;
        this.initiatives = initiatives;
    }

    @Override
    public boolean isOmUser() {
        return false;
    }

    @Override
    public boolean hasRightToInitiative(Long initiativeId) {
        return initiatives.contains(initiativeId);
    }

    @Override
    public Long getAuthorId() {
        return authorId;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    public String getSsn() {
        return ssn;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public Set<Long> getInitiatives() {
        return initiatives;
    }
}
