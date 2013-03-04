package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.web.Urls;

public class Municipality {
    private long id;
    private String finnishName;
    private String swedishName;

    public Municipality(long id, String finnishName, String swedishName) {
        this.id = id;
        this.finnishName = finnishName;
        this.swedishName = swedishName;
    }

    @JsonId(path= Urls.MUNICIPALITY)
    public long getId() {
        return id;
    }

    public String getFinnishName() {
        return finnishName;
    }

    public String getSwedishName() {
        return swedishName;
    }
}
