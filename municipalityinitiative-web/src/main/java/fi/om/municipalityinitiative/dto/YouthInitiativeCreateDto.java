package fi.om.municipalityinitiative.dto;

public class YouthInitiativeCreateDto {
    private Long municipality;

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public Long getMunicipality() {
        return municipality;
    }
}
