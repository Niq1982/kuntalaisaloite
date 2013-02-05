package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.newdto.ui.InitiativeUICreateDto;
import fi.om.municipalityinitiative.newdto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.validation.ParticipantValidationInfo;

public class ParticipantCreateDto {

    private Long municipalityInitiativeId;
    private boolean franchise;
    private boolean showName;
    private String participantName;
    private Long homeMunicipality;

    public static ParticipantCreateDto parse(InitiativeUICreateDto source, Long initiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setShowName(source.getShowName() == null ? false : source.getShowName());
        participantCreateDto.setParticipantName(source.getContactInfo().getName());
        participantCreateDto.setHomeMunicipality(source.getHomeMunicipality());
        participantCreateDto.setFranchise(solveFranchise(source));
        return participantCreateDto;
    }

    public static ParticipantCreateDto parse(ParticipantUICreateDto participant, Long initiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setShowName(participant.getShowName() == null ? false : participant.getShowName());
        participantCreateDto.setParticipantName(participant.getParticipantName());
        participantCreateDto.setHomeMunicipality(participant.getHomeMunicipality());
        participantCreateDto.setFranchise(solveFranchise(participant));
        return participantCreateDto;
    }

    private static boolean solveFranchise(ParticipantValidationInfo participant) {
        return participant.getHomeMunicipality().equals(participant.getMunicipality())
                && participant.getFranchise();
    }

    public Long getMunicipalityInitiativeId() {
        return municipalityInitiativeId;
    }

    public void setMunicipalityInitiativeId(Long municipalityInitiativeId) {
        this.municipalityInitiativeId = municipalityInitiativeId;
    }

    public void setFranchise(boolean franchise) {
        this.franchise = franchise;
    }

    public boolean isFranchise() {
        return franchise;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }

    public boolean isShowName() {
        return showName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

}
