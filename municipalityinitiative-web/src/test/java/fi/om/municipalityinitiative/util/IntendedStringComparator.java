package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.json.JsonJokuParseri;

import java.util.Comparator;

public class IntendedStringComparator implements Comparator<JsonJokuParseri.IndentedString> {

    @Override
    public int compare(JsonJokuParseri.IndentedString o1, JsonJokuParseri.IndentedString o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
