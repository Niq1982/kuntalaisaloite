package fi.om.municipalityinitiative.newdto;

public class ParticipantCreateDto extends ParticipantUIICreateDto {

    private Long municipalityInitiativeId;

    public Long getMunicipalityInitiativeId() {
        return municipalityInitiativeId;
    }

    public void setMunicipalityInitiativeId(Long municipalityInitiativeId) {
        this.municipalityInitiativeId = municipalityInitiativeId;
    }
}
