package fi.om.municipalityinitiative.dto.ui;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

// TODO: Validate max lengths
public class AuthorUIMessage {

    @NotNull
    private Long initiativeId;

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    private String contactEmail;

    @NotEmpty
    private String contactName;

    @NotEmpty
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
