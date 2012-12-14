package fi.om.municipalityinitiative.web;
import static fi.om.municipalityinitiative.web.RequestMessageType.SUCCESS;
import static fi.om.municipalityinitiative.web.RequestMessageType.WARNING;
public enum RequestMessage {
    // Success messages
    SAVE (SUCCESS),
    SEND_INVITATIONS (SUCCESS),
    SAVE_AND_SEND_INVITATIONS (SUCCESS, true),
    ACCEPT_INVITATION (SUCCESS),
    DECLINE_INVITATION (SUCCESS),
    SEND_TO_OM (SUCCESS),
    ACCEPT_BY_OM (SUCCESS),
    REJECT_BY_OM (SUCCESS),
    CONFIRM_CURRENT_AUTHOR (SUCCESS),
    DELETE_CURRENT_AUTHOR (SUCCESS),
    CONFIRM_VOTE (SUCCESS, true),
    SEND_TO_VRK (SUCCESS),
    SAVE_VRK_RESOLUTION (SUCCESS),
    REMOVE_SUPPORT_VOTES (SUCCESS),
    
    // Warnings
    SEND_INVITATIONS_FAILED (WARNING),
    OPEN_INVITATION_FAILED (WARNING),
    ALREADY_VOTED (WARNING),
    VOTING_NOT_ALLOWED (WARNING),
    ACCEPTING_INVITATIONS_NOT_ALLOWED (WARNING), 
    CONFIRM_VOTE_UNCHECKED (WARNING), 
    CONFIRM_REMOVAL_UNCHECKED (WARNING),
    ADULT_REQUIRED_AS_AUTHOR (WARNING)
    ;
    
    private final boolean modal;
    
    private final String messageKey;
    
    private final RequestMessageType type;
    
    RequestMessage(RequestMessageType type) {
        this(type, false);
    }
    
    RequestMessage(RequestMessageType type, boolean modal) {
        this.type = type;
        this.modal = modal;
        this.messageKey = type.name().toLowerCase() + "." + name().replaceAll("_","-").toLowerCase();
    }
    
    public String toString() {
        return messageKey;
    }
    
    public boolean isModal() {
        return modal;
    }

    public RequestMessageType getType() {
        return type;
    }
    
}
