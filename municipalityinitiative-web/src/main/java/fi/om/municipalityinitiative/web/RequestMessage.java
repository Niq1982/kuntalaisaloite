package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.web.RequestMessageType.SUCCESS;
import static fi.om.municipalityinitiative.web.RequestMessageType.WARNING;

public enum RequestMessage {
    // Success messages
    PREPARE (SUCCESS),
    SAVE_DRAFT (SUCCESS),
    UPDATE_INITIATIVE (SUCCESS),
    SEND_TO_REVIEW (SUCCESS),
    ACCEPT_INITIATIVE (SUCCESS),
    REJECT_INITIATIVE (SUCCESS),
    PUBLISH_AND_SEND (SUCCESS),
    SAVE (SUCCESS, true),
    START_COLLECTING(SUCCESS),
    SEND (SUCCESS, true),
    PARTICIPATE (SUCCESS),
    LOGOUT (SUCCESS),
    CONFIRM_PARTICIPATION (SUCCESS),
    INVITATION_SENT (SUCCESS),

    // Warning messages
    ALREADY_SENT (WARNING),
    PREPARE_CONFIRM_EXPIRED (WARNING);

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
