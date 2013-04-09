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

    public boolean isAllowSendToReview() {
        return initiative.getState() == InitiativeState.DRAFT;
    }

    public boolean isAllowOmAccept() {
        return initiative.getState() == InitiativeState.REVIEW;
    }

    public boolean isAllowUpdate() {
        return !isAllowEdit() && !initiative.getSentTime().isPresent();
    }

    public boolean isAllowSendToMunicipality() {
        return (initiative.getState().equals(InitiativeState.ACCEPTED) || initiative.getState().equals(InitiativeState.PUBLISHED))
                && initiative.getSentTime().isNotPresent();
    }
}
