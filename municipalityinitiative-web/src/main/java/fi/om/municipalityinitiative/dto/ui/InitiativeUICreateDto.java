package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.dto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.validation.InitiativeCreateValidMunicipalMembershipInfo;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ValidMunicipalMembership
public class InitiativeUICreateDto
        extends CreateDtoTimeValidation
        implements InitiativeCreateValidMunicipalMembershipInfo {

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_NAME_MAX)
    private String name;

    @NotEmpty
    @Size(max = InitiativeConstants.INITIATIVE_PROPOSAL_MAX)
    private String proposal;

    private Boolean municipalMembership;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;

    @Valid
    private ContactInfo contactInfo;

    private Boolean showName;

    private boolean collaborative = false; // Will be false if true is not received from the view

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
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

    public void setMunicipalMembership(Boolean municipalMembership) {
        this.municipalMembership = municipalMembership;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    @Override
    public boolean hasMunicipalMembership() {
        return municipalMembership;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
