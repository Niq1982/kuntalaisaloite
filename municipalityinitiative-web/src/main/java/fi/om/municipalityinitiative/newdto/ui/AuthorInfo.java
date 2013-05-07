package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Municipality;

public class AuthorInfo {
    private String name;
    private Municipality municipality;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

}
