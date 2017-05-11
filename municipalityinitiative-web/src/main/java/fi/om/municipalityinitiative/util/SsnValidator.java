package fi.om.municipalityinitiative.util;

import com.mysema.commons.lang.Assert;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SsnValidator {

    public static final String SSN_REGEX = "(\\d{2})(\\d{2})(\\d{2})([+-A])\\d{3}[0-9A-Z]";

    public static final Pattern SSN_PATTERN = Pattern.compile(SSN_REGEX);

    private static final int LEGAL_AGE = 18;

    public static String validateSsn(String ssn) {
        if (ssn == null) {
            throw invalidSSNException();
        }
        ssn = ssn.toUpperCase();
        if (SSN_PATTERN.matcher(ssn).matches()) {
            return ssn;
        } else {
            throw invalidSSNException();
        }

    }

    private static IllegalArgumentException invalidSSNException() {
        return new IllegalArgumentException("Invalid SSN");
    }

    public static boolean isAdult(LocalDate now, String ssn) {
        return getAge(now, ssn) >= LEGAL_AGE;
    }

    public static int getAge(LocalDate now, String ssn) {
        Assert.notNull(ssn, "ssn");
        Matcher m = SSN_PATTERN.matcher(ssn);
        if (m.matches()) {
            int dd = Integer.parseInt(m.group(1));
            int mm = Integer.parseInt(m.group(2));
            int yy = Integer.parseInt(m.group(3));
            int yyyy;
            char c = ssn.charAt(6);
            switch (c) {
                case '+':
                    yyyy = 1800 + yy;
                    break;
                case '-':
                    yyyy = 1900 + yy;
                    break;
                case 'A':
                    yyyy = 2000 + yy;
                    break;
                default:
                    throw invalidSSNException();
            }
             return Years.yearsBetween(new LocalDate(yyyy, mm, dd), now).getYears();
        } else {
            throw invalidSSNException();
        }
    }
}
