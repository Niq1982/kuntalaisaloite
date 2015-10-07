package fi.om.municipalityinitiative.dto.ui;


import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class MunicipalityDecisionDto {

    private List<MultipartFile> files;

    private String locale;

    @NotEmpty
    @Size(max = InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX)
    private String description;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
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

    public static MunicipalityDecisionDto build(String description) {
        MunicipalityDecisionDto municipalityDecisionDto = new MunicipalityDecisionDto();
        municipalityDecisionDto.setDescription(description);
        municipalityDecisionDto.setFiles(new ArrayList<MultipartFile>());
        return municipalityDecisionDto;
    }
}
