package fi.om.municipalityinitiative.newdto.service;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;

import java.util.Locale;

@JsonPropertyOrder(alphabetic = true)
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

    public String getNameFi() {
        return finnishName;
    }

    public String getNameSv() {
        return swedishName;
    }

    public String getLocalizedName(Locale locale) {
        return Locales.LOCALE_FI.equals(locale)
                ? getNameFi()
                : getNameSv();
    }
}
