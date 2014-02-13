package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthorUIMessage {

    @NotNull
    private Long initiativeId;

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String contactEmail;

    @NotEmpty
    @Size(max = InitiativeConstants.CONTACT_NAME_MAX)
    private String contactName;

    @NotEmpty
    @Size(max = InitiativeConstants.AUTHOR_MESSAGE_MAX)
    private String message;

    public Long getInitiativeId() {
        return initiativeId;
    }

    public void setInitiativeId(Long initiativeId) {
        this.initiativeId = initiativeId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
