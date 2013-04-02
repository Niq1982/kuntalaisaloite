package fi.om.municipalityinitiative.exceptions;

public class NotLoggedInException extends RuntimeException {

    public NotLoggedInException(String message) {
        super(message);
    }
}
