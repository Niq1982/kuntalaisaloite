package fi.om.municipalityinitiative.util;

import java.util.Locale;

public final class Locales {

    public static final String FI = "fi";

    public static final Locale LOCALE_FI = Locale.forLanguageTag(FI);
    
    public static final String SV = "sv";

    public static final Locale LOCALE_SV = Locale.forLanguageTag(SV);

    private Locales() {}
    
    public static Locale getAltLocale(Locale locale) {
        if (LOCALE_FI.equals(locale)) {
            return LOCALE_SV;
        }
        else {
            return LOCALE_FI;
        }
    }
    
}
