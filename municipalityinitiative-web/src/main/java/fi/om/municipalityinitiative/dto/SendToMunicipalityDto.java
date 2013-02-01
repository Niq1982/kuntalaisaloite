package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;

import javax.validation.Valid;

public class SendToMunicipalityDto {

    @Valid
    ContactInfo contactInfo;

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
