package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.CreateDtoTimeValidation;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import fi.om.municipalityinitiative.validation.ValidMunicipalMembershipInfo;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ValidMunicipalMembership
public class ParticipantUICreateDto
        extends CreateDtoTimeValidation
        implements ValidMunicipalMembershipInfo {

    @NotEmpty
    private String participantName;

    @NotNull
    private Long homeMunicipality;

    private Boolean showName;

    private Boolean municipalMembership;

    @NotNull
    private Long municipality;

    public void setMunicipality(Long municipality) {
        this.municipality = municipality;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String name) {
        this.participantName = name;
    }

    @Override
    public Long getHomeMunicipality() {
        return homeMunicipality;
    }

    @Override
    public Long getMunicipality() {
        return municipality;
    }

    public void setHomeMunicipality(Long homeMunicipality) {
        this.homeMunicipality = homeMunicipality;
    }

    public Boolean getMunicipalMembership() {
        return municipalMembership;
    }

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    @Override
    public boolean hasMunicipalMembership() {
        return municipalMembership != null && Boolean.TRUE.equals(municipalMembership);
    }

    public void setMunicipalMembership(Boolean municipalMembership) {
        this.municipalMembership = municipalMembership;
    }
}
