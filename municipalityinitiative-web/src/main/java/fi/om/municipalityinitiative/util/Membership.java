package fi.om.municipalityinitiative.util;

public enum Membership {
    community,
    // company, // Removed, but still existing in DB because of limitation of enum value deletion in postgres.
    property,
    service,
    none
}
