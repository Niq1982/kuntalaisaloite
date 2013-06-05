package fi.om.municipalityinitiative.web;

import fi.om.municipalityinitiative.exceptions.AccessDeniedException;

public class CSRFException extends AccessDeniedException {

    private static final long serialVersionUID = 2151739773568509601L;

    public CSRFException() {
        super();
    }

    public CSRFException(String message) {
        super(message);
    }

}
