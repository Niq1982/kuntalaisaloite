package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Maybe;

public class AuthorInfo {
    private String name;
    private Maybe<Municipality> municipality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Maybe<Municipality> getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Maybe<Municipality> municipality) {
        this.municipality = municipality;
    }

}
