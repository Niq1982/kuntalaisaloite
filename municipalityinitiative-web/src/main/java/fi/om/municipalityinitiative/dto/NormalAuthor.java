package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.service.id.NormalAuthorId;

public class NormalAuthor extends Author<NormalAuthorId> {

    @Override
    public boolean isVerified() {
        return false;
    }
}

