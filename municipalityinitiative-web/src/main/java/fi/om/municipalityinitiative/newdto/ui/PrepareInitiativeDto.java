package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.validation.InitiativeCreateParticipantValidationInfo;

import javax.validation.constraints.NotNull;

public class PrepareInitiativeDto
        extends CreateDtoTimeValidation
        implements InitiativeCreateParticipantValidationInfo {

    private boolean collectable;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;

    private Boolean franchise;

    @Override
    public boolean isCollectable() {
        return collectable;
    }

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    @Override
    public Boolean getFranchise() {
        return franchise;
    }

    @Override
    public Boolean getMunicipalMembership() {
        return null;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
    }
}
