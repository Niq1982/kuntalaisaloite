package fi.om.municipalityinitiative.newdto.ui;

import javax.validation.Valid;

public class SendToMunicipalityDto {

    @Valid
    ContactInfo contactInfo;
    
    private String comment;

    private String managementHash;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getManagementHash() {
        return managementHash;
    }

    public void setManagementHash(String managementHash) {
        this.managementHash = managementHash;
    }

    public static SendToMunicipalityDto parse(String managementHash, ContactInfo contactInfo) {
        SendToMunicipalityDto sendToMunicipalityDto = new SendToMunicipalityDto();
        sendToMunicipalityDto.setManagementHash(managementHash);
        sendToMunicipalityDto.setContactInfo(contactInfo);
        return sendToMunicipalityDto;
    }
}
