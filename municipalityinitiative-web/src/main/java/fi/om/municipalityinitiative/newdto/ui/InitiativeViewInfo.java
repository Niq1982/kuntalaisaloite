package fi.om.municipalityinitiative.newdto.ui;

import fi.om.municipalityinitiative.newdto.service.Initiative;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;
import fi.om.municipalityinitiative.util.Maybe;
import org.joda.time.LocalDate;

public class InitiativeViewInfo {

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

}
