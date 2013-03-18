package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.dto.InitiativeConstants;
import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.validation.InitiativeCreateParticipantValidationInfo;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PrepareInitiativeDto
        extends CreateDtoTimeValidation
        implements InitiativeCreateParticipantValidationInfo {

    private boolean collectable;

    @NotNull
    private Long municipality;

    @NotNull
    private Long homeMunicipality;

    private Boolean franchise;

    @NotEmpty
    @Pattern(regexp = ContactInfo.EMAIL_PATTERN)
    @Size(max = InitiativeConstants.CONTACT_EMAIL_MAX)
    private String authorEmail;

    @Override
    public boolean isCollectable() {
        return collectable;
    }

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    @Override
    public Boolean getFranchise() {
        return franchise;
    }

    @Override
    public Boolean getMunicipalMembership() {
        return null;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public void setFranchise(Boolean franchise) {
        this.franchise = franchise;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }
}
