package fi.om.municipalityinitiative.util.hash;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RandomHashGenerator {

    private static String previous;

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";

    private static SecureRandom rnd = new SecureRandom();

    private static AtomicInteger increment = new AtomicInteger(0);

    public static void main(String[] params) {
        for (int i = 0; i < 1000; ++i) {
            System.out.println(randomString(40));
        }
    }

    public synchronized static String randomString( int len )
    {
        if (increment.incrementAndGet() % 100 == 0) {
            rnd.setSeed(rnd.generateSeed(1024));
        }

        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        previous = sb.toString();
        return previous;
    }

    public static String longHash() {
        return RandomHashGenerator.randomString(40);
    }

    public static String shortHash() {
        return RandomHashGenerator.randomString(20);
    }

    // For tests
    static String getPrevious() {
        return previous;
    }

}
