package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.VerifiedUserId;

public class VerifiedParticipant extends Participant<VerifiedUserId> {

    @Override
    public boolean isVerified() {
        return true;
    }
}
