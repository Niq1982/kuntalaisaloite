package fi.om.municipalityinitiative.util;

import java.util.Random;

public class RandomHashGenerator {

    private static String previous;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static Random rnd = new Random();

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
}
