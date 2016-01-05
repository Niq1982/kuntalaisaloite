package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.validation.InitiativeWithLocationInformation;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.ValidLocation;
import fi.om.municipalityinitiative.validation.VerifiedInitiative;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@ValidLocation(groups = {VerifiedInitiative.class, NormalInitiative.class})
public class InitiativeDraftUIEditDto implements InitiativeWithLocationInformation {

    // Not editable after set
    private Municipality municipality;

    // Editable by author via ui

    @NotEmpty(groups = {VerifiedInitiative.class, NormalInitiative.class})
    @Size(max = InitiativeConstants.INITIATIVE_NAME_MAX, groups = {VerifiedInitiative.class, NormalInitiative.class})
    private String name;

    @NotEmpty(groups = {VerifiedInitiative.class, NormalInitiative.class})
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX, groups = {VerifiedInitiative.class, NormalInitiative.class})
    private String proposal;

    @Size(max = InitiativeConstants.INITIATIVE_EXTRA_INFO_MAX, groups = {VerifiedInitiative.class, NormalInitiative.class})
    private String extraInfo;

    @Valid
    private ContactInfo contactInfo;

    @Min(value = 0, groups = {VerifiedInitiative.class, NormalInitiative.class} )
    private int externalParticipantCount;

    private List<Location> locations = new ArrayList<Location>();

    private String videoUrl;

    public InitiativeDraftUIEditDto() {
        // For freemarker
    }

    public static InitiativeDraftUIEditDto parse(Initiative initiative, ContactInfo contactInfo, List<Location> locations) {
        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto();
        editDto.setExtraInfo(initiative.getExtraInfo());
        editDto.setName(initiative.getName());
        editDto.setProposal(initiative.getProposal());
        editDto.municipality = initiative.getMunicipality();
        editDto.setContactInfo(new ContactInfo(contactInfo));
        editDto.setExternalParticipantCount(initiative.getExternalParticipantCount());
        editDto.setLocations(locations);
        if (initiative.getVideoUrl().isPresent()) {
            editDto.setVideoUrl(initiative.getVideoUrl().getValue());
        }
        return editDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Municipality getMunicipality() {
        return municipality;
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

    public List<Location> getLocations(){
        return this.locations;
    }

    public void setLocations(List<Location> locations){
        this.locations = locations;
    }


    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
