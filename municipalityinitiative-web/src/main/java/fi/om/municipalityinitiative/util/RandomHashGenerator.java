package fi.om.municipalityinitiative.util;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Random;

public class RandomHashGenerator {

    private static String previous;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";

    private static Random rnd = new Random(new Date().getTime());

    public synchronized static String randomString( int len )
    {
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        previous = sb.toString();
        return previous;
    }

    // This is for testing purposes.
    public static String getPrevious() {
        return previous;
    }

    public static String longHash() {
        return RandomHashGenerator.randomString(40);
    }

    public static String shortHash() {
        return RandomHashGenerator.randomString(20);
    }
}
