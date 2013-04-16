package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembershipInfo;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public abstract class ParticipantUICreateBase extends CreateDtoTimeValidation
        implements ValidMunicipalMembershipInfo {

    @NotNull
    private Long homeMunicipality;

    private Membership municipalMembership;
    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String participantEmail;

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

    public final String getParticipantEmail() {
        return participantEmail;
    }

    public final void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }


    public enum Membership {
        community,
        company,
        property,
        none
    }
}
