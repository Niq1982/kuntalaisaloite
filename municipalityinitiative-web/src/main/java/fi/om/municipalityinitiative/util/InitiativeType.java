package fi.om.municipalityinitiative.util;

public enum InitiativeType {
    UNDEFINED,
    SINGLE,
    COLLABORATIVE,
    COLLABORATIVE_COUNCIL,
    COLLABORATIVE_CITIZEN;

    public static boolean isCollaborative(InitiativeType type) {
        switch (type) {
            case COLLABORATIVE:
            case COLLABORATIVE_COUNCIL:
            case COLLABORATIVE_CITIZEN:
                return true;
            default:
                return false;
        }

    }

    public boolean isCollaborative() {
        return isCollaborative(this);
    }
}
