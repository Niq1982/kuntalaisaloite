package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InitiativeUIEditDto {

    // Not editable after set
    private Long id;
    private Municipality municipality;

    // Hidden field which must match with database
    private String managementHash;

    // Editable by author via ui
    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_NAME_MAX)
    private String name;

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String proposal;

    @Valid
    private ContactInfo contactInfo;

    @NotNull
    private Boolean showName;

    private InitiativeUIEditDto() {
        // For freemarker
    }

    public InitiativeUIEditDto(Municipality municipality) {
        this.municipality = municipality;
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
}
