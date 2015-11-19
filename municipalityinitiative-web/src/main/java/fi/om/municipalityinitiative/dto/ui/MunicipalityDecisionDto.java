package fi.om.municipalityinitiative.dto.ui;


import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class MunicipalityDecisionDto {



    private List<FileWithName> files = new ArrayList<FileWithName>();

    private String locale;

    @Size(max = InitiativeConstants.MUNICIPALITY_DECISION_MAX)
    private String description;

    public List<FileWithName> getFiles() {
        return files;
    }

    public void setFiles(List<FileWithName> files) {
        this.files = files;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static MunicipalityDecisionDto build(Maybe<String> description) {
        MunicipalityDecisionDto municipalityDecisionDto = new MunicipalityDecisionDto();
        if (description.isPresent()) {
            municipalityDecisionDto.setDescription(description.getValue());
        }

        municipalityDecisionDto.setFiles(new ArrayList<FileWithName>());
        return municipalityDecisionDto;
    }

    public static class FileWithName {
        private MultipartFile file;
        private String name;

        public FileWithName() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MultipartFile getFile() {
            return file;
        }

        public void setFile(MultipartFile file) {
            this.file = file;
        }
    }
}
