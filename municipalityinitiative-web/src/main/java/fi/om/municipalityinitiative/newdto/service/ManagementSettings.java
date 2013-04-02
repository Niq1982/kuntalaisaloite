package fi.om.municipalityinitiative.newdto.service;

import fi.om.municipalityinitiative.service.AccessDeniedException;
import fi.om.municipalityinitiative.util.InitiativeState;

public class ManagementSettings {

    private Initiative initiative;

    public ManagementSettings(Initiative initiative) {
        this.initiative = initiative;
    }

    public boolean isAllowEdit() {
        return initiative.getState() == InitiativeState.DRAFT;
    }

    public void assertManagementHash(String expectedManagementHash) {
        if (!initiative.getManagementHash().equals(expectedManagementHash)) {
            throw new AccessDeniedException("Invalid managementHash");
        }
    }

    public boolean isAllowUpdate() {
        switch (initiative.getState()) {
            case REVIEW:
            case ACCEPTED:
                return true;
            default:
                return false;
        }
    }
}
