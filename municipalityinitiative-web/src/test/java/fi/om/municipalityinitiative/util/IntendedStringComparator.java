package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.json.JsonStringParser;

import java.util.Comparator;

public class IntendedStringComparator implements Comparator<JsonStringParser.IndentedString> {

    @Override
    public int compare(JsonStringParser.IndentedString o1, JsonStringParser.IndentedString o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
