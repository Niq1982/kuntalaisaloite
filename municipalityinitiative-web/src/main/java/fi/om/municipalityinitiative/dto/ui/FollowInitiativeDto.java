package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FollowInitiativeDto {

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String participantEmail;

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }
}
