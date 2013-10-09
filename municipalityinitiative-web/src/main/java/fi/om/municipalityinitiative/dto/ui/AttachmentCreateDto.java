package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

public class AttachmentCreateDto {

    private MultipartFile image;
    private String locale;

    @NotEmpty
    @Max(InitiativeConstants.ATTACHMENT_DESCRIPTION_MAX)
    private String description;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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
}
