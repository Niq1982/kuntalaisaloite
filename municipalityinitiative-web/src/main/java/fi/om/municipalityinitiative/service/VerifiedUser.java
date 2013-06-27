package fi.om.municipalityinitiative.service;

import fi.om.municipalityinitiative.dto.ui.ContactInfo;

public class VerifiedUser {
    private long userId;
    private ContactInfo contactInfo;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
