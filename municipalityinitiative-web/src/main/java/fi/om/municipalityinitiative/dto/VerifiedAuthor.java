package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.service.id.VerifiedUserId;

public class VerifiedAuthor extends Author<VerifiedUserId> {

    @Override
    public boolean isVerified() {
        return true;
    }

}
