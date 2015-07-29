package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.exceptions.InvalidLocationException;
import fi.om.municipalityinitiative.validation.InitiativeWithLocationInformation;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.ValidLocation;
import fi.om.municipalityinitiative.validation.VerifiedInitiative;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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

    private Double locationLat;

    private Double locationLng;

    private String locationDescription;

    public InitiativeDraftUIEditDto() {
        // For freemarker
    }

    public static InitiativeDraftUIEditDto parse(Initiative initiative, ContactInfo contactInfo) {
        InitiativeDraftUIEditDto editDto = new InitiativeDraftUIEditDto();
        editDto.setExtraInfo(initiative.getExtraInfo());
        editDto.setName(initiative.getName());
        editDto.setProposal(initiative.getProposal());
        editDto.municipality = initiative.getMunicipality();
        editDto.setContactInfo(new ContactInfo(contactInfo));
        editDto.setExternalParticipantCount(initiative.getExternalParticipantCount());
        if (initiative.getLocation().isPresent() && initiative.getLocationDescription().isPresent()) {
            editDto.locationLat = initiative.getLocation().getValue().getLat();
            editDto.locationLng = initiative.getLocation().getValue().getLng();
            editDto.locationDescription = initiative.getLocationDescription().getValue();
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

    public Location getLocation() {
        if (this.locationLat != null && this.locationLng != null){
            return new Location(this.locationLat, this.locationLng);
        }
        else if (locationLat == null && locationLng == null) {
            return null;
        }
        else {
            throw new InvalidLocationException("Invalid location. Location not saved");
        }

    }

    public void setLocation(Location location) {this.locationLat = location.getLat(); this.locationLng = location.getLng(); }

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

    public Double getLocationLat(){
        return this.locationLat;
    }

    public void setLocationLat(Double locationLat){
        this.locationLat = locationLat;
    }

    public Double getLocationLng(){
        return this.locationLng;
    }

    public void setLocationLng(Double locationLng){
        this.locationLng = locationLng;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }
}
