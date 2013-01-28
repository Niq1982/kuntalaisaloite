package fi.om.municipalityinitiative.newdto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@ValidFranchise
@ValidMunicipalMembership
public class ParticipantUIICreateDto implements ParticipantFranchise {

    @NotEmpty
    private String participantName;

    @NotNull
    private Long homeMunicipality;

    @NotNull
    private Boolean showName;

    private Boolean franchise;

    private Boolean municipalMembership;

    @NotNull
    private Long municipality;

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String name) {
        this.participantName = name;
    }

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
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
    public Boolean getFranchise() {
        return franchise;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
    }

    @Override
    public Boolean getMunicipalMembership() {
        return municipalMembership;
    }

    public void setMunicipalMembership(Boolean municipalMembership) {
        this.municipalMembership = municipalMembership;
    }
}
