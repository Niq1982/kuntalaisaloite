package fi.om.municipalityinitiative.dao;

public class InvitationNotValidException extends RuntimeException {

    public InvitationNotValidException(String message) {
        super(message);
    }
}
