package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;

import java.util.Optional;

public class AuthorInfo {
    private String name;
    private Optional<Municipality> municipality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<Municipality> getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Optional<Municipality> municipality) {
        this.municipality = municipality;
    }

}
