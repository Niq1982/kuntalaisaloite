package fi.om.municipalityinitiative.dto.ui;

import com.google.common.base.Strings;
import fi.om.municipalityinitiative.dto.service.Initiative;
import fi.om.municipalityinitiative.dto.service.Location;
import fi.om.municipalityinitiative.dto.service.Municipality;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.InitiativeType;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

public final class InitiativeViewInfo {

    private final Initiative initiative;

    private InitiativeViewInfo(Initiative initiative) {
        this.initiative = initiative;
    }

    public static InitiativeViewInfo parse(Initiative initiative) {
        return new InitiativeViewInfo(initiative);
    }

    public String getName() {
        return initiative.getName();
    }

    public String getProposal() {
        return initiative.getProposal();
    }

    public LocalDate getCreateTime() {
        return initiative.getCreateTime();
    }

    public Long getId() {
        return initiative.getId();
    }

    public boolean isCollaborative() {
        return initiative.isCollaborative();
    }
    
    public boolean isSingle(){
        return initiative.getType() == InitiativeType.SINGLE;
    }
    
    public boolean isSent() {
        return initiative.isSent();
    }

    public Maybe<LocalDate> getSentTime() {
        return initiative.getSentTime();
    }

    public InitiativeState getState() {
        return initiative.getState();
    }

    public Municipality getMunicipality() {
        return initiative.getMunicipality();
    }

    public LocalDate getStateTime() {
        return initiative.getStateTime();
    }

    public String getExtraInfo() {
        return initiative.getExtraInfo();
    }

    public FixState getFixState() {
        return initiative.getFixState();
    }

    public boolean hasNeverBeenSaved() {
        return Strings.isNullOrEmpty(initiative.getName());
    }

    public int getExternalParticipantCount() {
        return initiative.getExternalParticipantCount();
    }

    public boolean isVerifiable() {
        return initiative.getType().isVerifiable();
    }
    
    public InitiativeType getType() {
        return initiative.getType();
    }

    public Maybe<Long> getYouthInitiativeId() {
        return initiative.getYouthInitiativeId();
    }

    public Maybe<Location> getLocation() {return initiative.getLocation();}

    public Maybe<String> getLocationDescription() {return initiative.getLocationDescription();}

    public ParticipantCount getParticipantCount() {
        ParticipantCount participantCount = new ParticipantCount();
        participantCount.setPrivateNames(initiative.getParticipantCount() - initiative.getParticipantCountPublic());
        participantCount.setPublicNames(initiative.getParticipantCountPublic());
        return participantCount;
    }
}
