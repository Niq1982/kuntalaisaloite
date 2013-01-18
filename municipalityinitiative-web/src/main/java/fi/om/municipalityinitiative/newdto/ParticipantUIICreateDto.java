package fi.om.municipalityinitiative.newdto;

import javax.validation.constraints.NotNull;

public class ParticipantUIICreateDto {

    @NotNull
    private String name;

    @NotNull
    private Long homeMunicipality;

    @NotNull
    private Boolean showName;

    @NotNull
    private Boolean franchise;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
