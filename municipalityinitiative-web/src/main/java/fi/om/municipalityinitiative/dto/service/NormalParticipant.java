package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.NormalParticipantId;
import fi.om.municipalityinitiative.util.Membership;

public class NormalParticipant extends Participant<NormalParticipantId>{

    private Membership membership;

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }
}
