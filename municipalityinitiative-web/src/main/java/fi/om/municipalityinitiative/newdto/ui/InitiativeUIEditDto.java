package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.newdto.service.Municipality;

public class InitiativeUIEditDto extends CreateDtoTimeValidation {

    // Not editable after set
    private Long id;
    private Municipality municipality;

    // Hidden field which must match with database
    private String managementHash;

    // Editable by author via ui
    private String name;
    private String proposal;
    private ContactInfo contactInfo;
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
