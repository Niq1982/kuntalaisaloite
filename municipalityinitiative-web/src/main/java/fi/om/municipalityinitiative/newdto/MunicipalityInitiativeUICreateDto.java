package fi.om.municipalityinitiative.newdto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MunicipalityInitiativeUICreateDto {
    
    /**
     *  Using @Pattern instead of @Email, because hibernate's email validation was quite not good enough.
     *  Hibernate's Email validator passes emails like: "address@domain", "address@domain.com."
     *  
     *  Regexp is from: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
     *  - Added char '+' and '-' for the original regexp. 
     */
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-\\+]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @NotEmpty
    private String name;

    private String proposal;
    
    private Boolean municipalMembership;

    private Boolean franchise;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;
    
    @NotEmpty
    private String contactName;
    
    @NotEmpty
    @Pattern(regexp = EMAIL_PATTERN)
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
