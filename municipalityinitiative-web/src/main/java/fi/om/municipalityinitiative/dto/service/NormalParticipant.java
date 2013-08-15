package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.service.id.NormalParticipantId;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.Membership;

public class NormalParticipant extends Participant<NormalParticipantId>{

    private Membership membership;

    private Maybe<Municipality> homeMunicipality;

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public Maybe<Municipality> getHomeMunicipality() {
        return homeMunicipality;
    }

    public void setHomeMunicipality(Maybe<Municipality> homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }
}
