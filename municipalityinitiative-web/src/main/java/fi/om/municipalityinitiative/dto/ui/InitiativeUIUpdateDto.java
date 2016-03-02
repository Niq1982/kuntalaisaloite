package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.validation.InitiativeWithLocationInformation;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.ValidLocation;
import fi.om.municipalityinitiative.validation.VerifiedInitiative;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ValidLocation(groups = {VerifiedInitiative.class, NormalInitiative.class})
public class InitiativeUIUpdateDto implements InitiativeWithLocationInformation {

    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String extraInfo;

    @Min(0)
    private int externalParticipantCount;

    @Valid
    private ContactInfo contactInfo;

    private List<Location> locations = new ArrayList<>();


    private String videoUrl;

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


    @Override
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
