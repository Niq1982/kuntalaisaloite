package fi.om.municipalityinitiative.json;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonJokuParseri {

    public static List<IndentedString> toParts(String jsonData) {
        jsonData = addEndlines(jsonData, "{", "},", ",", "[", "]");
        jsonData = jsonData.replace("}", "\n}");

        Iterable<String> parts = Splitter.onPattern("\n")
                .trimResults()
                .omitEmptyStrings()
                .split(jsonData);

        List<IndentedString> results = new ArrayList<IndentedString>();

        Map<Integer, String> parents = Maps.newHashMap();

        int intend = 0;
        for (String part : parts) {

            if (part.contains("}")) {
                --intend;
            }

            saveParent(parents, intend, part);

            results.add(new IndentedString(intend, part, getParent(intend, parents)));

            if (part.contains("{")) {
                ++intend;
            }
        }

        return results;
    }

    private static String getParent(int intend, Map<Integer, String> currentParentLocalizationKey) {
        if (intend > 0) {
            return currentParentLocalizationKey.get(intend-1);
        }
        return "";
    }

    private static void saveParent(Map<Integer, String> currentParentLocalizationKey, int intend, String part) {
        if (!currentParentLocalizationKey.containsKey(intend))
            currentParentLocalizationKey.put(intend, "");
        if (part.contains("\""))
            currentParentLocalizationKey.put(intend, part);
    }

    private static String addEndlines(String jsonData, String ... parts) {
        for (String part : parts) {
            jsonData = jsonData.replace(part, part+"\n");
        }
        return jsonData;
    }

    public static class IndentedString {
        int indent;
        String value;
        private String parent;

        private IndentedString(int indent, String value, String parent) {
            this.indent = indent;
            this.value = value;
            this.parent = parent;
        }

        public int getIndent() {
            return indent*2;
        }

        public String getValue() {
            return value;
        }

        public String getParent() {
            return parent;
        }

        public String getLocalizationKey() {
            if (hasProperty(value))
                return "api"+parseValue(this.parent) + parseValue(value);
            return "api";
        }

        private static String parseValue(String value) {
            if (hasProperty(value)) {
                return "."+ getProperty(value);
            }
            return "";
        }

        private static String getProperty(String value) {
            return value.split("\"")[1];
        }

        private static boolean hasProperty(String value) {
            return value.contains("\"");
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
