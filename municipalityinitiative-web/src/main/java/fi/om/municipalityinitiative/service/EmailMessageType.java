package fi.om.municipalityinitiative.service;

public enum EmailMessageType {
    ACCEPTED_BY_OM_AND_SENT,
    ACCEPTED_BY_OM,
    REJECTED_BY_OM,
    INVITATION_REJECTED,
    INVITATION_ACCEPTED,
    PUBLISHED_COLLECTING,
    SENT_TO_MUNICIPALITY,
    AUTHOR_ACCEPTED,
    ;

    private final String messageKeySuffix;
    
    EmailMessageType() {
        this.messageKeySuffix = name();
    }
    
    public String toString() {
        return messageKeySuffix;
    }
}
