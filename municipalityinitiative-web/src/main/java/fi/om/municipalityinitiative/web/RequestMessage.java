package fi.om.municipalityinitiative.web;

import static fi.om.municipalityinitiative.web.RequestMessageType.SUCCESS;
public enum RequestMessage {
    // Success messages - Municipality
    SAVE_AND_SEND (SUCCESS),
    SAVE (SUCCESS, true),
    
    // Success messages
    //SAVE (SUCCESS),

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
