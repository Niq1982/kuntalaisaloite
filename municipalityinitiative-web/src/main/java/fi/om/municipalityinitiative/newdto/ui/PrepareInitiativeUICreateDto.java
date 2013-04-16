package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidMunicipalMembership
public class PrepareInitiativeUICreateDto extends ParticipantUICreateBase {

    // Is set as null if normal initiative because we do not know if creator wants to gather any other people
    private InitiativeType initiativeType;

    @NotNull
    Long municipality;

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
