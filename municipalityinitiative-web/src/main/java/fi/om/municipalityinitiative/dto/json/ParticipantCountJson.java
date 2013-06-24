package fi.om.municipalityinitiative.dto.json;

import fi.om.municipalityinitiative.dto.ui.ParticipantCount;

public class ParticipantCountJson {

    ParticipantCount participantCount;
    int externalParticipantCount;

    public ParticipantCountJson(ParticipantCount participantCount, int externalParticipantCount) {
        this.participantCount = participantCount;
        this.externalParticipantCount = externalParticipantCount;
    }

    public long getPublicNames() {
        return participantCount.getPublicNames();
    }

    public long getPrivateNames() {
        return participantCount.getPrivateNames();
    }

    public long getTotal() {
        return participantCount.getTotal() + externalParticipantCount;
    }

    public long getExternalNames() {
        return externalParticipantCount;
    }

}
