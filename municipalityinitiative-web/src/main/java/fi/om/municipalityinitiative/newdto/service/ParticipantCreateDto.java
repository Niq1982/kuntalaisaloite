package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;

public class ParticipantCreateDto extends ParticipantUICreateDto {

    private Long municipalityInitiativeId;

    public static ParticipantCreateDto parse(InitiativeUICreateDto source, Long municipalityInitiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(municipalityInitiativeId); // TODO: Fix null possibilities after valdiations are complete
        participantCreateDto.setFranchise(source.getFranchise() == null ? false : source.getFranchise());

        participantCreateDto.setShowName(source.getShowName() == null ? false : source.getShowName());
        participantCreateDto.setParticipantName(source.getContactInfo().getName());
        participantCreateDto.setHomeMunicipality(source.getHomeMunicipality());
        return participantCreateDto;
    }

    public Long getMunicipalityInitiativeId() {
        return municipalityInitiativeId;
    }

    public void setMunicipalityInitiativeId(Long municipalityInitiativeId) {
        this.municipalityInitiativeId = municipalityInitiativeId;
    }
}
