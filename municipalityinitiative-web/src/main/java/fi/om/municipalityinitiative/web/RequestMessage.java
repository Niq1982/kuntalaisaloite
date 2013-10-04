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
    START_COLLECTING(SUCCESS),
    SEND (SUCCESS, true),
    PARTICIPATE (SUCCESS),
    PARTICIPATE_VERIFIABLE (SUCCESS, true),
    LOGOUT (SUCCESS),
    CONFIRM_PARTICIPATION (SUCCESS),
    INVITATION_SENT (SUCCESS),
    AUTHOR_DELETED (SUCCESS),
    CONFIRM_INVITATION_ACCEPTED (SUCCESS),
    CONFIRM_INVITATION_REJECTED (SUCCESS),
    PARTICIPANT_DELETED (SUCCESS),

    INFORMATION_SAVED(SUCCESS),

    ATTACHMENT_ADDED(SUCCESS),
    ATTACHMENT_DELETED(SUCCESS),

    // Warning messages
    ALREADY_SENT (WARNING),
    PREPARE_CONFIRM_EXPIRED (WARNING),

    EDITOR_SAVE_DRAFT(SUCCESS),
    EDITOR_PUBLISH_DRAFT(SUCCESS),
    EDITOR_RESTORE_PUBLISHED(SUCCESS),
    EDITOR_UPLOAD_IMAGE(SUCCESS),
    MANAGEMENT_HASH_RENEWED(SUCCESS),
    AUTHOR_MESSAGE_ADDED(SUCCESS),
    AUTHOR_MESSAGE_SENT(SUCCESS),
    INVALID_HOME_MUNICIPALITY(WARNING);

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
