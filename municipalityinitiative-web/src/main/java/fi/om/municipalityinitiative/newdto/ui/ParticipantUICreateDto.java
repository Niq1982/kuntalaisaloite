package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidMunicipalMembership
public class ParticipantUICreateDto
        extends ParticipantUICreateBase {

    @NotEmpty
    private String participantName;

    private Boolean showName;

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String participantEmail;

    @NotNull
    private Long municipality;

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String name) {
        this.participantName = name;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    // FIXME: Municipality of the initiative should always be set via back, not front.
    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }
}
