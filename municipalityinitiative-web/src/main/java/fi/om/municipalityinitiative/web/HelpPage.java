package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.util.Locales;

public enum HelpPage {
    NEWS("tiedotteet", "aktuellt"),
    CONTACT("yhteystiedot", "kontaktuppgifter"),
    ORGANIZERS("ohjeet-vastuuhenkilolle", "anvisningar-for-ansvarspersoner"),
    PARTICIPANTS("ohje-osallistujalle", "anvisningar-for-deltagare"),
    INITIATIVE_TYPES("kuntalaisaloitteen-muodot", "formen-av-ett-invanarinitiativ"),
    TERMS_OF_USE("palvelun-kayttoehdot", "anvandarvillkor");

    private final String uriFi;
    private final String uriSv;

    HelpPage(String uriFi, String uriSv) {
        this.uriFi = uriFi;
        this.uriSv = uriSv;
    }

    public String getUri(String locale) {
        return Locales.LOCALE_SV.toLanguageTag().equals(locale)
                ? uriSv
                : uriFi;
    }


}
