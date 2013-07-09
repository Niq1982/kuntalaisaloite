package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.service.id.VerifiedUserId;

public class VerifiedAuthor extends Author<VerifiedUserId> {

    private VerifiedUserId id;

    @Override
    public VerifiedUserId getId() {
        return id;
    }

    public void setId(VerifiedUserId verifiedUserId) {
        this.id = verifiedUserId;
    }
}
