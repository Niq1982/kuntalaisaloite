package fi.om.municipalityinitiative.dto.ui;

import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

public class InitiativeListInfo {
    private Long id;
    private String name;

    private Municipality municipality;

    private boolean collaborative;
    private Maybe<LocalDate> sentTime;
    private long participantCount;
    private InitiativeType type;
    private InitiativeState state;
    private LocalDate stateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public Maybe<LocalDate> getSentTime() {
        return sentTime;
    }

    public void setSentTime(Maybe<LocalDate> sentTime) {
        this.sentTime = sentTime;
    }

    public void setParticipantCount(long participantCount) {
        this.participantCount = participantCount;
    }

    public long getParticipantCount() {
        return participantCount;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public InitiativeType getType() {
        return type;
    }

    public void setType(InitiativeType typeMaybe) {
        this.type = typeMaybe;
    }

    public void setState(InitiativeState state) {
        this.state = state;
    }

    public boolean isPublic() {
        return state == InitiativeState.PUBLISHED;
    }

    public boolean isVerifiable() {
        return InitiativeType.isVerifiable(type);
    }

    public void setStateTime(LocalDate stateTime) {
        this.stateTime = stateTime;
    }

    public LocalDate getStateTime() {
        return stateTime;
    }
}
