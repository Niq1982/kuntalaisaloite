package fi.om.municipalityinitiative.newweb;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class MunicipalityInitiativeUICreateDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String proposal;
    
    private Boolean municipalMembership;

    private Boolean franchise;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;
    
    private String contactName;
    
    private String contactEmail;
    
    private String contactPhone;
    
    private String contactAddress;
    
    private Boolean showName;

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
    
    public Boolean getFranchise() {
        return franchise;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
    }
    
    public Long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public Boolean getMunicipalMembership() {
        return municipalMembership;
    }
}
