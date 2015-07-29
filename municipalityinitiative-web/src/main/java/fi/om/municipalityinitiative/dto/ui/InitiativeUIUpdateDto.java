package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class InitiativeUIUpdateDto {

    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String extraInfo;

    @Min(0)
    private int externalParticipantCount;

    @Valid
    private ContactInfo contactInfo;

    private Double locationLat;

    private Double locationLng;

    private String locationDescription;

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setExternalParticipantCount(int externalParticipantCount) {
        this.externalParticipantCount = externalParticipantCount;
    }

    public int getExternalParticipantCount() {
        return externalParticipantCount;
    }

    public Double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(Double locationLat) {
        this.locationLat = locationLat;
    }

    public Double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(Double locationLng) {
        this.locationLng = locationLng;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
