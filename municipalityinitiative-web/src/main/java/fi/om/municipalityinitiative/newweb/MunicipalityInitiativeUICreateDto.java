package fi.om.municipalityinitiative.newweb;

public class MunicipalityInitiativeUICreateDto {

    private String name;
    
    private String proposal;
    
    private boolean municipalMembership;
    
    private boolean franchise;
    
    private long municipality;
    
    private long homeMunicipality;
    
    private String contactName;
    
    private String contactEmail;
    
    private String contactPhone;
    
    private String contactAddress;
    
    private boolean showName;

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
    
    public boolean isMunicipalMembership() {
        return municipalMembership;
    }

    public void setMunicipalMembership(boolean municipalMembership) {
        this.municipalMembership = municipalMembership;
    }
    
    public boolean isFranchise() {
        return franchise;
    }

    public void setFranchise(boolean franchise) {
        this.franchise = franchise;
    }
    
    public long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(long municipality) {
        this.municipality = municipality;
    }

    public long getHomeMunicipality() {
        return homeMunicipality;
    }

    public void setHomeMunicipality(long homeMunicipality) {
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

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }
}
