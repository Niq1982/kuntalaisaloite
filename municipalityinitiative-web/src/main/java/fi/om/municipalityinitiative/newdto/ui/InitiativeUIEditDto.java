package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InitiativeUIEditDto {

    // Not editable after set
    private Municipality municipality;
    private InitiativeState state;

    // Hidden field which must match with database
    private String managementHash;

    // Editable by author via ui
    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_NAME_MAX)
    private String name;

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String proposal;
    
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String extraInfo;

    @Valid
    private ContactInfo contactInfo;

    @NotNull
    private Boolean showName;

    public InitiativeUIEditDto(Municipality municipality, InitiativeState initiativeState) {
        this.municipality = municipality;
        this.state = initiativeState;
    }

    public InitiativeUIEditDto() {
        // For freemarker
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

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public String getManagementHash() {
        return managementHash;
    }

    public void setManagementHash(String managementHash) {
        this.managementHash = managementHash;
    }
    
    public InitiativeState getState() {
        return state;
    }
}
