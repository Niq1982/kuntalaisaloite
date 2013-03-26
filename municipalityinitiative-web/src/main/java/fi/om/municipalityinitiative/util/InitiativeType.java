package fi.om.municipalityinitiative.util;

public enum InitiativeType {
    SINGLE,
    COLLABORATIVE,
    COLLABORATIVE_COUNCIL,
    COLLABORATIVE_CITIZEN;


    public static boolean isCollectable(Maybe<InitiativeType> type) {
        return type.isPresent() && isCollectable(type.get());
    }
    public static boolean isCollectable(InitiativeType type) {
        return type != null && !type.equals(InitiativeType.SINGLE);
    }
}
