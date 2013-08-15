package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.VerifiedUserId;

public class VerifiedParticipant extends Participant<VerifiedUserId> {

    private boolean verified;

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
