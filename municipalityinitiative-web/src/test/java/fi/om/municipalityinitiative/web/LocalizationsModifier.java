package fi.om.municipalityinitiative.web;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.aspectj.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class LocalizationsModifier {

    private final static String resourcesDir = System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/";
    private final static String[] resourceFiles = {"messages.properties", "messages_sv.properties"};
    private final static String[] exludePrefix = {"api.", "editor.", "success.editor"};

    private static List<ResourceFile> resources = Lists.newArrayList();

    public static void main(String[] moimoiii) throws IOException {

        for (String resourceFile : resourceFiles) {
            ResourceFile file = new ResourceFile();

            BufferedReader br = new BufferedReader(new FileReader(resourcesDir + resourceFile));
            String line;
            while ((line = br.readLine()) != null) {
                file.addResourceLine(line);
            }
            br.close();

            resources.add(file);
        }


        ResourceFile masterFile = resources.get(0);
        for (int i = 1; i < resources.size(); ++i) {
            System.out.println("Handling localization file: " + resourceFiles[i]);
            StringBuilder linesToWriteToOrderedPropertyFile = new StringBuilder();

            ResourceFile otherFile = resources.get(i);
            for (ResourceFileLine masterFileLine : masterFile.lines) {

                if (masterFileLine.type == LineType.LOCALIZATION) {
                    Optional<ResourceFileLine> otherLocalization = otherFile.findLocalizationLine(masterFileLine.key);
                    if (!otherLocalization.isPresent()) {
                        if (isNotExcludeKey(masterFileLine.key)) {
                            System.out.println("Localization missing: " + masterFileLine.key + " = " + masterFileLine.localization);
                        }
                    }
                    else {
                        linesToWriteToOrderedPropertyFile
                                .append(masterFileLine.key)
                                .append(" = ")
                                .append(otherLocalization.get().localization)
                                .append("\n");
                    }
                }
                else {
                    linesToWriteToOrderedPropertyFile
                            .append(masterFileLine.toString())
                            .append("\n");
                }

            }

            FileUtil.writeAsString(new File(resourcesDir + resourceFiles[i]), linesToWriteToOrderedPropertyFile.toString());
        }


    }

    private static boolean isNotExcludeKey(String key) {
        for (String s : exludePrefix) {
            if (key.startsWith(s)) {
                return false;
            }
        }
        return true;
    }

    private static class ResourceFile {
        private List<ResourceFileLine> lines = Lists.newArrayList();

        public void addResourceLine(String line) {
            lines.add(new ResourceFileLine(line));
        }

        public Optional<ResourceFileLine> findLocalizationLine(String key) {
            for (ResourceFileLine line : lines) {
                if (line.type == LineType.LOCALIZATION && line.key.equals(key)) {
                    return Optional.of(line);
                }
            }
            return Optional.absent();
        }
    }

    private static class ResourceFileLine {

        final public LineType type;
        final public String key;
        final public String localization;

        public ResourceFileLine(String line) {
            line = line.trim();
            if (Strings.isNullOrEmpty(line)) {
                this.type = LineType.EMPTY;
                key = null;
                localization = null;
            }
            else if (line.charAt(0) == '#') {
                this.type = LineType.COMMENT;
                key = null;
                localization = line;
            }
            else {

                int indexOfEquals = line.indexOf('=');
                if (indexOfEquals == -1) {
                    throw new RuntimeException("Invalid localization line: \n" + line);
                }

                this.type = LineType.LOCALIZATION;
                key = line.substring(0, indexOfEquals).trim();
                localization = line.substring(indexOfEquals+1).trim();
            }

        }

        @Override
        public String toString() {

            switch (type) {
                case EMPTY:
                    return "";
                case COMMENT:
                    return localization;
                case LOCALIZATION:
                    return key + " = " + localization;
                default:
                    throw new RuntimeException("Invalid type: " + type);
            }
        }
    }

    public enum LineType {
        EMPTY,
        COMMENT,
        LOCALIZATION
    }
}
