package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.exceptions.OperationNotAllowedException;

public class SecurityUtil {
    public static void assertAllowance(String s, boolean allowed) {
        if (!allowed) {
            throw new OperationNotAllowedException("Operation not allowed: " + s);
        }
    }
}
