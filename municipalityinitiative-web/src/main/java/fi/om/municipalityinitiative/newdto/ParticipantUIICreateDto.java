package fi.om.municipalityinitiative.newdto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class ParticipantUIICreateDto {

    @NotEmpty
    private String participantName;

    @NotNull
    private Long homeMunicipality;

    @NotNull
    private Boolean showName;

    @NotNull
    private Boolean franchise;
    
    // TODO: Added by mikkole. Do we really need this?
    private Boolean municipalMembership;

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String name) {
        this.participantName = name;
    }

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

    public Boolean getFranchise() {
        return franchise;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
    }
    
    public Boolean getMunicipalMembership() {
        return municipalMembership;
    }

    public void setMunicipalMembership(Boolean municipalMembership) {
        this.municipalMembership = municipalMembership;
    }
}
