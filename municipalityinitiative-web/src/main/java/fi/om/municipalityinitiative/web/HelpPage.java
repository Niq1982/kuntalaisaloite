package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.util.Locales;

public enum HelpPage {
    NEWS("tiedotteet", "aktuellt"),
    CONTACT("yhteystiedot", "kontaktuppgifter"),
    ORGANIZERS("ohjeet-vastuuhenkilolle", "anvisningar-for-ansvarspersoner"),
    PARTICIPANTS("ohje-osallistujalle", "ohje-osallistujalle_sv"),
    INITIATIVE_STEPS("aloitteen-vaiheet", "initiativets-skeden");

    private String uriFi;
    private String uriSv;

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
