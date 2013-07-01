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

    public static boolean isVerifiable(InitiativeType type) {
        switch (type) {
            case COLLABORATIVE_COUNCIL:
            case COLLABORATIVE_CITIZEN:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNotVerifiable(InitiativeType type) {
        return !isVerifiable(type);
    }

    public boolean isNotVerifiable() {
        return !isVerifiable();
    }

    public boolean isVerifiable() {
        return isVerifiable(this);
    }

    public boolean isCollaborative() {
        return isCollaborative(this);
    }
}
