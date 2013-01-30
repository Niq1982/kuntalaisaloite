package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.validation.InitiativeCreateParticipantValidationInfo;
import fi.om.municipalityinitiative.validation.ValidCreateFranchise;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ValidMunicipalMembership
@ValidCreateFranchise
public class InitiativeUICreateDto implements InitiativeCreateParticipantValidationInfo {

    @NotEmpty
    private String name;

    private String proposal;

    private Boolean municipalMembership;

    private Boolean franchise;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;

    private ContactInfo contactInfo;

    private Boolean showName;

    private Boolean collectable;

    public Boolean getCollectable() {
        return collectable;
    }

    public void setCollectable(Boolean collectable) {
        this.collectable = collectable;
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
    public Boolean getFranchise() {
        return franchise;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
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
    public Boolean getMunicipalMembership() {
        return municipalMembership;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
