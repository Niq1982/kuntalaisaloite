package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.util.InitiativeType;

public class PrepareSafeInitiativeUICreateDto {

    private InitiativeType initiativeType;

    private Long municipality;

    private String participantEmail;

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setVerifiedAuthorEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

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
}
