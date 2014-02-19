package fi.om.municipalityinitiative.server;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JettyServer {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String JETTY_PORT = "jetty.port";
    private static final String JETTY_THREAD_POOL_COUNT = "jetty.thread.pool";

    public static void main(String[] args) {
        try {
            startService(Integer.parseInt(getSystemProperty(JETTY_PORT)), getSystemProperty(SPRING_PROFILES_ACTIVE)).join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Server startService(int port, String profile) throws IOException {
        PropertyConfigurator.configure(configurationFile("log4j.properties"));
        Server server = new Server(port);
        server.setThreadPool(new QueuedThreadPool(Integer.parseInt(getSystemProperty(JETTY_THREAD_POOL_COUNT))));

        WebAppContext context = new WebAppContext();
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp/");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");

        if (profile != null) {
            context.setInitParameter("spring.profiles.active", profile);
        }

        server.setHandler(context);

        try {
            server.start();
            return server;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String configurationFile(String s) throws FileNotFoundException {

        File file = new File(new File(getSystemProperty("java.class.path")).getParentFile().getAbsoluteFile().getPath() + "/config/" + s);
        if (!file.exists()) {
            throw new FileNotFoundException("Configuration file not found: " + file.getPath());
        }
        return file.getPath();
    }

    protected static String getSystemProperty(String variableName) {
        String s = System.getProperty(variableName);
        if (s == null) {
            throw new NullPointerException("System property was null: " + variableName);
        }
        return s;
    }

}
