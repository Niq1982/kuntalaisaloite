package fi.om.municipalityinitiative.newdto.json;

import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.web.Urls;

public class MunicipalityJson {

    private Municipality municipality;

    public MunicipalityJson(Municipality municipality) {
        this.municipality = municipality;
    }

    @JsonId(path= Urls.MUNICIPALITY)
    public Long getId() {
        return municipality.getId();
    }

    public String getFinnishName() {
        return municipality.getFinnishName();
    }

    public String getSwedishName() {
        return municipality.getSwedishName();
    }

}
