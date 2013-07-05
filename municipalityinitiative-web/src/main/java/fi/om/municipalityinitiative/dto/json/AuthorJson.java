package fi.om.municipalityinitiative.dto.json;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.dto.ui.AuthorInfo;

public class AuthorJson {

    private final String name;
    private final Municipality municipality;

    public AuthorJson(AuthorInfo publicAuthor) {
        name = publicAuthor.getName();
        municipality = publicAuthor.getMunicipality().isPresent() ? publicAuthor.getMunicipality().get() : null;
    }

    public String getName() {
        return name;
    }

    public Municipality getMunicipality() {
        return municipality;
    }
}
