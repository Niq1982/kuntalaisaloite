package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;
import fi.om.municipalityinitiative.service.id.VerifiedUserId;

public class VerifiedUser {
    private VerifiedUserId userId;
    private ContactInfo contactInfo;

    public VerifiedUserId getUserId() {
        return userId;
    }

    public void setUserId(VerifiedUserId userId) {
        this.userId = userId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
