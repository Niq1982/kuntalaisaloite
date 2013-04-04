package fi.om.municipalityinitiative.service;

public enum EmailMessageType {
    ACCEPTED_BY_OM_AND_SENT,
    ACCEPTED_BY_OM,
    REJECTED_BY_OM,
    INVITATION_ACCEPTED,
    INVITATION_REJECTED,
    PUBLISHED_COLLECTING,
    SENT_TO_MUNICIPALITY
    ;

    private final String messageKeySuffix;
    
    EmailMessageType() {
        this.messageKeySuffix = name().replaceAll("_","-").toLowerCase();
    }
    
    public String toString() {
        return messageKeySuffix;
    }
}
