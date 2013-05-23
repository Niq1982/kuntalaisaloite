package fi.om.municipalityinitiative.dto.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fi.om.municipalityinitiative.json.JsonId;
import fi.om.municipalityinitiative.util.Locales;
import fi.om.municipalityinitiative.web.Urls;

import java.util.Locale;

@JsonPropertyOrder(alphabetic = true)
public class Municipality {
    private final Long id;
    private final String finnishName;
    private final String swedishName;
    private final boolean active;

    public Municipality(long id, String finnishName, String swedishName, Boolean active) {
        this.id = id;
        this.finnishName = finnishName;
        this.swedishName = swedishName;
        this.active = active;
    }

    @JsonId(path= Urls.MUNICIPALITY)
    public Long getId() {
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
    
    public String getName(String locale) {
        return getLocalizedName(Locale.forLanguageTag(locale));
    }

    @JsonIgnore
    public boolean isActive() {
        return active;
    }
}
