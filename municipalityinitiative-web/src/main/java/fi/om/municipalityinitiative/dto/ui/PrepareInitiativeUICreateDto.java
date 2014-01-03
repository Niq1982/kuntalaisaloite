package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import fi.om.municipalityinitiative.validation.VerifiedInitiative;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ValidMunicipalMembership(groups = NormalInitiative.class)
public class PrepareInitiativeUICreateDto extends ParticipantUICreateBase implements Serializable {

    @NotNull(groups = {NormalInitiative.class, VerifiedInitiative.class})
    private InitiativeType initiativeType;

    @NotNull(groups = {NormalInitiative.class, VerifiedInitiative.class})
    Long municipality;

    @NotEmpty(groups = NormalInitiative.class)
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN, groups = NormalInitiative.class)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX, groups = NormalInitiative.class)
    private String participantEmail;

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public InitiativeType getInitiativeType() {
        return initiativeType;
    }

    public void setInitiativeType(InitiativeType initiativeType) {
        this.initiativeType = initiativeType;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }
}
