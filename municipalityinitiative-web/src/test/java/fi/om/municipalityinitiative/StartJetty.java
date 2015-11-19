package fi.om.municipalityinitiative;

import fi.om.municipalityinitiative.server.JettyServer;
import org.eclipse.jetty.server.Server;


public class StartJetty {



    public static Server startService(int port, String springProfile) {
        try {
            return JettyServer.start(new JettyServer.JettyProperties(
                    port,
                    10,
                    springProfile,
                    "config/log4j.properties",
                    "src/main/webapp/"));

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);

        }
    }

    public static void main(String[] args) throws Throwable {
        startService(8080, "dev,disableSecureCookie").join();

    }
}
