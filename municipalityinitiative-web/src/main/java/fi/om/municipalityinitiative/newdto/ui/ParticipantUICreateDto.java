package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.validation.ValidMunicipalMembership;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@ValidMunicipalMembership
public class ParticipantUICreateDto
        extends ParticipantUICreateBase {

    @NotEmpty
    private String participantName;

    private Boolean showName;

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
}
