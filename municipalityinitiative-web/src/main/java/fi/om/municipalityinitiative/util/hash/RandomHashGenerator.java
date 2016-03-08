package fi.om.municipalityinitiative.util.hash;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

public abstract class RandomHashGenerator {

    private static final Object lock = new Object();

    private static String previous;

    private static final String ALLOWED_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";

    private static SecureRandom rnd = new SecureRandom();

    private static AtomicLong increment = new AtomicLong(0);

    public static String randomString( int len ) {
        synchronized (lock) {
            if (increment.incrementAndGet() % 100 == 0) {
                rnd.setSeed(rnd.generateSeed(1024));
            }

            StringBuilder builder = new StringBuilder( len );
            for( int i = 0; i < len; i++ ) {
                builder.append( ALLOWED_CHARACTERS.charAt( rnd.nextInt(ALLOWED_CHARACTERS.length()) ) );
            }

            previous = builder.toString();
            return previous;
        }
    }

    public static String longHash() {
        return randomString(40);
    }

    public static String shortHash() {
        return randomString(20);
    }

    // For tests
    static String getPrevious() {
        return previous;
    }

}
