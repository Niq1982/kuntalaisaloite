package fi.om.municipalityinitiative.service.email;

public enum EmailMessageType {
    SENT_TO_REVIEW,
    SENT_FIX_TO_REVIEW,
    ACCEPTED_BY_OM_AND_SENT,
    ACCEPTED_BY_OM,
    REJECTED_BY_OM,
    ACCEPTED_BY_OM_FIX,
    INVITATION_REJECTED,
    INVITATION_ACCEPTED,
    PUBLISHED_COLLECTING,
    SENT_TO_MUNICIPALITY, // This is only used when sending single to municipality.
                        // When sending collaborative, the authors will receive the same email that municipality receives
    AUTHOR_ACCEPTED
    ;

    private final String messageKeySuffix;
    
    EmailMessageType() {
        this.messageKeySuffix = name();
    }
    
    public String toString() {
        return messageKeySuffix;
    }
}
