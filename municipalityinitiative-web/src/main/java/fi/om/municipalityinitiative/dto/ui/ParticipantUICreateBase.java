package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.util.Membership;
import fi.om.municipalityinitiative.validation.NormalInitiative;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembershipInfo;

import javax.validation.constraints.NotNull;

public abstract class ParticipantUICreateBase extends CreateDtoTimeValidation
        implements ValidMunicipalMembershipInfo {

    @NotNull(groups = NormalInitiative.class)
    private Long homeMunicipality;

    private Membership municipalMembership;

    @Override
    public final Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public final boolean hasMunicipalMembership() {
        return municipalMembership != null && !municipalMembership.equals(Membership.none);
    }

    public final void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public final Membership getMunicipalMembership() {
        return municipalMembership;
    }

    public final void setMunicipalMembership(Membership municipalMembership) {
        this.municipalMembership = municipalMembership;
    }

}
