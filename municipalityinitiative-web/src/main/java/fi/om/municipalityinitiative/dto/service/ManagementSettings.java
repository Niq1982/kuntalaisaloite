package fi.om.municipalityinitiative.dto.service;

import fi.om.municipalityinitiative.util.FixState;
import fi.om.municipalityinitiative.util.InitiativeState;

public class ManagementSettings {

    private Initiative initiative;

    // TODO: FixState should block almost all allowances

    private ManagementSettings(Initiative initiative) {
        this.initiative = initiative;
    }

    public static ManagementSettings of(Initiative initiative) {
        return new ManagementSettings(initiative);
    }

    public boolean isAllowEdit() {
        return initiative.getState() == InitiativeState.DRAFT;
    }

    public boolean isAllowSendToReview() {
        return initiative.getState() == InitiativeState.DRAFT;
    }

    public boolean isAllowOmAccept() {
        return initiative.getState() == InitiativeState.REVIEW || initiative.getFixState() == FixState.REVIEW;
    }

    public boolean isAllowUpdate() {
        return !isAllowEdit() && !initiative.getSentTime().isPresent();
    }

    public boolean isAllowSendToMunicipality() {
        return (initiative.getState().equals(InitiativeState.ACCEPTED) || initiative.getState().equals(InitiativeState.PUBLISHED))
                && initiative.getSentTime().isNotPresent()
                && initiative.getFixState() == FixState.OK;
    }

    public boolean isAllowPublish() {
        return initiative.getState().equals(InitiativeState.ACCEPTED) && initiative.getFixState().equals(FixState.OK);
    }

    public boolean isAllowParticipate() {
        return !(initiative.getState() != InitiativeState.PUBLISHED
                || initiative.getSentTime().isPresent()
                || !initiative.isCollaborative()
                || initiative.getFixState() != FixState.OK);
    }

    public boolean isAllowInviteAuthors() {
        return initiative.getSentTime().isNotPresent() && initiative.getFixState() == FixState.OK;
    }

    public boolean isAllowOmSendBackForFixing() {
        if ((initiative.getState() == InitiativeState.PUBLISHED || initiative.getState() == InitiativeState.ACCEPTED)
                && initiative.getSentTime().isNotPresent()
                && initiative.getFixState() == FixState.OK) {
            return true;
        }

        return false;

    }


    public boolean isAllowSendFixToReview() {
        return initiative.getFixState() == FixState.FIX;
    }
}