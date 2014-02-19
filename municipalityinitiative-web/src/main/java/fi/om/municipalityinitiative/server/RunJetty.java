package fi.om.municipalityinitiative.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

public class RunJetty {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String JETTY_PORT = "jetty.port";
    private static final String JETTY_THREAD_POOL_COUNT = "jetty.thread.pool";

    public static void main(String[] args) throws Throwable {
        try {
            JettyServer.start(new JettyServer.JettyProperties(
                    Integer.valueOf(getSystemProperty(JETTY_PORT)),
                    Integer.valueOf(getSystemProperty(JETTY_THREAD_POOL_COUNT)),
                    getSystemProperty(SPRING_PROFILES_ACTIVE),
                    configurationFile("log4j.properties")
            ));
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    private static String configurationFile(String fileName) throws FileNotFoundException {

        File parentFile = new File(getSystemProperty("java.class.path")).getParentFile();
        if (parentFile == null) {
            throw new InvalidParameterException("Give the whole path to jar-file instead of just: " + getSystemProperty("java.class.path"));
        }
        File file = new File(parentFile.getAbsoluteFile().getPath() + "/config/" + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException("Configuration file not found: " + file.getPath());
        }
        return file.getPath();
    }

    private static String getSystemProperty(String variableName) {
        String s = System.getProperty(variableName);
        if (s == null) {
            throw new NullPointerException("System property was null: " + variableName);
        }
        return s;
    }
}
