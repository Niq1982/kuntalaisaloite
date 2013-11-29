package fi.om.municipalityinitiative.util;

public enum InitiativeState {
    // Initiative is draft when it's created
    DRAFT,

    // Initiative is sent to OM for review
    REVIEW,

    // OM has accepted initiative and it's returned for author(s) for management
    ACCEPTED,

    // Initiative is published
    PUBLISHED,

    // NOTE: There is no state for SENT_TO_MUNICIPALITY - it's sent state is considered according to Initiative.sentTime being present
}
