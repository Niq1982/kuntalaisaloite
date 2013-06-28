package fi.om.municipalityinitiative.dto.user;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;

import java.util.Set;

public class VerifiedUser extends User{

    private final String ssn;
    private final ContactInfo contactInfo;
    private final Set<Long> initiatives;

    VerifiedUser(String ssn, ContactInfo contactInfo, Set<Long> initiatives) {
        this.ssn = ssn;

        // This is needed after we've logged in and participating or creating an initiative.
        // Data must be updated always when updating something at the UI
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
