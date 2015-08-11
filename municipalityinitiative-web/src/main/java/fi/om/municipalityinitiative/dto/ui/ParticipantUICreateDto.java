package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.NormalInitiativeVerifiedUser;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidMunicipalMembership(groups = {NormalInitiative.class, NormalInitiativeVerifiedUser.class})
public class ParticipantUICreateDto
        extends ParticipantUICreateBase {

    @NotEmpty(groups = NormalInitiative.class)
    private String participantName;

    private Boolean showName;

    @NotEmpty(groups = NormalInitiative.class)
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN, groups = NormalInitiative.class)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX, groups = NormalInitiative.class)
    private String participantEmail;

    @NotNull(groups = {NormalInitiative.class, NormalInitiativeVerifiedUser.class})
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
        Assert.notNull(municipality);
        return municipality;
    }

    public void assignInitiativeMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }
}
