package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.util.InitiativeType;

public class PrepareSafeInitiativeUICreateDto {

    private InitiativeType initiativeType;

    private Long municipality;

    public InitiativeType getInitiativeType() {
        return initiativeType;
    }

    public void setInitiativeType(InitiativeType initiativeType) {
        this.initiativeType = initiativeType;
    }

    public Long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public static PrepareSafeInitiativeUICreateDto parse(PrepareInitiativeUICreateDto prepareDataForVetuma) {
        PrepareSafeInitiativeUICreateDto uiCreateDto = new PrepareSafeInitiativeUICreateDto();
        uiCreateDto.setInitiativeType(prepareDataForVetuma.getInitiativeType());
        uiCreateDto.setMunicipality(prepareDataForVetuma.getMunicipality());
        return uiCreateDto;
    }
}
