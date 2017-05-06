package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.validation.NormalInitiativeEmailUser;
import fi.om.municipalityinitiative.validation.NormalInitiativeVerifiedUser;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import fi.om.municipalityinitiative.validation.VerifiedInitiative;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@ValidMunicipalMembership(groups = {NormalInitiativeEmailUser.class, NormalInitiativeVerifiedUser.class})
public class PrepareInitiativeUICreateDto extends ParticipantUICreateBase implements Serializable {

    @NotNull(groups = {NormalInitiativeEmailUser.class, VerifiedInitiative.class, NormalInitiativeVerifiedUser.class})
    private InitiativeType initiativeType;

    @NotNull(groups = {NormalInitiativeEmailUser.class, VerifiedInitiative.class, NormalInitiativeVerifiedUser.class})
    Long municipality;

    @NotEmpty(groups = NormalInitiativeEmailUser.class)
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN, groups = NormalInitiativeEmailUser.class)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX, groups = NormalInitiativeEmailUser.class)
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
