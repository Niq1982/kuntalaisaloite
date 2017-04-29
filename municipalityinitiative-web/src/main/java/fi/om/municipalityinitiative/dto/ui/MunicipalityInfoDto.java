package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.Locales;

import java.util.Locale;

public class MunicipalityInfoDto extends Municipality {

    private String email;
    private String description;
    private String descriptionSv;

    public MunicipalityInfoDto(long id, String finnishName, String swedishName, Boolean active, String email, String description, String descriptionSv) {
        super(id, finnishName, swedishName, active);
        this.email = email;
        this.description = description;
        this.descriptionSv = descriptionSv;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getDescriptionSv() { return descriptionSv; }

    public void setDescriptionSv(String descriptionSv) { this.descriptionSv = descriptionSv; }

    public String getLocalizedDescription(String localeStr) {
        Locale locale = Locale.forLanguageTag(localeStr);
        String desc =  Locales.LOCALE_FI.equals(locale)
                ? getDescription()
                : getDescriptionSv();
        return desc;
    }
}
