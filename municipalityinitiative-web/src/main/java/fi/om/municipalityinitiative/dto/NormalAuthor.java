package fi.om.municipalityinitiative.dto;

import fi.om.municipalityinitiative.service.id.NormalAuthorId;

public class NormalAuthor extends Author {

    private NormalAuthorId id;

    @Override
    public NormalAuthorId getId() {
        return id;
    }

    public void setId(NormalAuthorId id) {
        this.id = id;
    }
}
