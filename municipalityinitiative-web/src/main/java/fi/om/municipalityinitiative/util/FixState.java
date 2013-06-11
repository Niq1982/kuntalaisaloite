package fi.om.municipalityinitiative.util;


/**
 * FixState is usually OK.
 * Only when OM rejects initiative _after_ they have accepted it once,
 * FixState is used so we can still keep track of the initiatives basic state, which could be ACCEPTED or PUBLISHED.
 */
public enum FixState {

    FIX,
    REVIEW,
    OK
}
