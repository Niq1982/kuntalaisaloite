package fi.om.municipalityinitiative.newweb;

public class MunicipalityInitiativeUICreateDto {

    private String name;
    
    private String proposal;
    
    private boolean municipalMembership;
    
    private boolean franchise;
    
    private long municipality;
    
    private long homeMunicipality;

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
    
}
