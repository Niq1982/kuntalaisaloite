package fi.om.municipalityinitiative.util;

public enum InitiativeType {
    UNDEFINED,
    SINGLE,
    COLLABORATIVE,
    COLLABORATIVE_COUNCIL,
    COLLABORATIVE_CITIZEN;

    public static boolean isCollectable(InitiativeType type) {
        switch (type) {
            case COLLABORATIVE:
            case COLLABORATIVE_COUNCIL:
            case COLLABORATIVE_CITIZEN:
                return true;
            default:
                return false;
        }

    }

    public boolean isCollectable() {
        return isCollectable(this);
    }
}
