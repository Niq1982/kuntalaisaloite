package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.NormalParticipantId;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;

public class NormalParticipant extends Participant<NormalParticipantId>{

    @Override
    public boolean isVerified() {
        return false;
    }
}
