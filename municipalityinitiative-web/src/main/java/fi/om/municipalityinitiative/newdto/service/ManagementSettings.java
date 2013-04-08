package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.util.InitiativeState;

public class ManagementSettings {

    private Initiative initiative;

    public ManagementSettings(Initiative initiative) {
        this.initiative = initiative;
    }

    public boolean isAllowEdit() {
        return initiative.getState() == InitiativeState.DRAFT;
    }

    public boolean isAllowUpdate() {
        return !isAllowEdit() && !initiative.getSentTime().isPresent();
    }
}
