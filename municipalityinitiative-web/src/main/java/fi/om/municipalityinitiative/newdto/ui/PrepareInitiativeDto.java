package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembershipInfo;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidMunicipalMembership
public class PrepareInitiativeDto
        extends CreateDtoTimeValidation
        implements ValidMunicipalMembershipInfo {

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;
    
    // Is set as null if normal initiative because we do not know if creator wants to gather any other people
    private InitiativeType initiativeType;

    // Is validated at ValidMunicipalMembershipValidator because may ne null if municipalities don't differ
    private Membership municipalMembership;

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String authorEmail;

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }
    
    public InitiativeType getInitiativeType() {
        return initiativeType;
    }

    public void setInitiativeType(InitiativeType initiativeType) {
        this.initiativeType = initiativeType;
    }

    @Override
    public boolean hasMunicipalMembership() {
        return municipalMembership != null && !municipalMembership.equals(Membership.none);
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Membership getMunicipalMembership() {
        return municipalMembership;
    }

    public void setMunicipalMembership(Membership municipalMembership) {
        this.municipalMembership = municipalMembership;
    }

    public enum Membership {
        community,
        company,
        property,
        none
    }
}
