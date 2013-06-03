package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.dto.ui.AuthorUIMessage;

public class AuthorMessage {

    private Long initiativeId;
    private String contactEmail;
    private String contactName;
    private String message;
    private String confirmationCode;

    public AuthorMessage() {
    }

    public AuthorMessage(AuthorUIMessage authorUIMessage, String confirmationCode) {
        this.initiativeId = authorUIMessage.getInitiativeId();
        this.contactEmail = authorUIMessage.getContactEmail();
        this.contactName = authorUIMessage.getContactName();
        this.message = authorUIMessage.getMessage();
        this.confirmationCode = confirmationCode;
    }

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

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
