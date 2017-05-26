package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.util.InitiativeType;

public class PrepareVerifiedInitiativeUICreateDto {

    private InitiativeType initiativeType;

    private Long municipality;

    private Long userGivenHomeMunicipality;

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

    public void setUserGivenHomeMunicipality(Long userGivenHomeMunicipality) {
        this.userGivenHomeMunicipality = userGivenHomeMunicipality;
    }

    public Long getUserGivenHomeMunicipality() {
        return userGivenHomeMunicipality;
    }

    public static PrepareVerifiedInitiativeUICreateDto parse(PrepareInitiativeUICreateDto prepareDataForVetuma) {
        PrepareVerifiedInitiativeUICreateDto uiCreateDto = new PrepareVerifiedInitiativeUICreateDto();
        uiCreateDto.setInitiativeType(prepareDataForVetuma.getInitiativeType());
        uiCreateDto.setMunicipality(prepareDataForVetuma.getMunicipality());
        uiCreateDto.setUserGivenHomeMunicipality(prepareDataForVetuma.getHomeMunicipality());
        return uiCreateDto;
    }
}
