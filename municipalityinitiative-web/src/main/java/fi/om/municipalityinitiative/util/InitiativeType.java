package fi.om.municipalityinitiative.util;

public enum InitiativeType {

    // Only normal initiative may have UNDEFINED type until author decides what to do with it
    UNDEFINED,

    // Normal initiative is marked as SINGLE when author decides it will be sent straight to municipality
    SINGLE,

    // Normal initiative is marked as collaborative when the author decides to start collecting participants
    COLLABORATIVE,

    // Type for verified initiative is chosen by author when initiative draft is created.
    COLLABORATIVE_COUNCIL,
    COLLABORATIVE_CITIZEN;

    public static boolean isCollaborative(InitiativeType type) {
        if (type == null) {
            return false;
        }
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
        if (type == null) {
            return false;
        }
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
