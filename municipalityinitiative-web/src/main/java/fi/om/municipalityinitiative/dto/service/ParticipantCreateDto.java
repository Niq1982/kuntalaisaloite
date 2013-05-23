package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.dto.ui.AuthorInvitationUIConfirmDto;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateBase;
import fi.om.municipalityinitiative.dto.ui.ParticipantUICreateDto;
import fi.om.municipalityinitiative.util.Membership;

public class ParticipantCreateDto {

    private Long municipalityInitiativeId;
    private boolean showName;
    private String participantName;
    private Long homeMunicipality;
    private String email;
    private Membership municipalMembership;

    public static ParticipantCreateDto parse(ParticipantUICreateDto participant, Long initiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();

        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setShowName(participant.getShowName() == null ? false : participant.getShowName());
        participantCreateDto.setParticipantName(participant.getParticipantName());
        participantCreateDto.setHomeMunicipality(participant.getHomeMunicipality());
        participantCreateDto.setEmail(participant.getParticipantEmail());
        participantCreateDto.setMunicipalMembership(solveMembership(participant));
        return participantCreateDto;
    }

    public static ParticipantCreateDto parse(AuthorInvitationUIConfirmDto participant, Long initiativeId) {
        ParticipantCreateDto participantCreateDto = new ParticipantCreateDto();
        participantCreateDto.setMunicipalityInitiativeId(initiativeId);
        participantCreateDto.setShowName(participant.getContactInfo().isShowName());
        participantCreateDto.setParticipantName(participant.getContactInfo().getName());
        participantCreateDto.setHomeMunicipality(participant.getHomeMunicipality());
        participantCreateDto.setEmail(participant.getContactInfo().getEmail());
        participantCreateDto.setMunicipalMembership(solveMembership(participant));
        return participantCreateDto;
    }

    private static Membership solveMembership(ParticipantUICreateBase participant) {
        return participant.getMunicipality().equals(participant.getHomeMunicipality()) ?
                Membership.none : participant.getMunicipalMembership();
    }

    public Long getMunicipalityInitiativeId() {
        return municipalityInitiativeId;
    }

    public void setMunicipalityInitiativeId(Long municipalityInitiativeId) {
        this.municipalityInitiativeId = municipalityInitiativeId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Membership getMunicipalMembership() {
        return municipalMembership;
    }

    public void setMunicipalMembership(Membership municipalMembership) {
        this.municipalMembership = municipalMembership;
    }
}
