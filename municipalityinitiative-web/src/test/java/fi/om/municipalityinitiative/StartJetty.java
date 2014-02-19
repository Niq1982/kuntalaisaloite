package fi.om.municipalityinitiative;

import fi.om.municipalityinitiative.conf.PropertyNames;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

public class StartJetty {
    
    public static final int PORT = 8443;

    public static void main(String[] args) throws Throwable {

//        String log4jPath = System.getProperty("user.dir") + "/src/test/resources/log4j.properties";
//        JettyServer.start(new JettyServer.JettyProperties(PORT,10,"dev",log4jPath));

        try {
            System.setProperty(PropertyNames.optimizeResources, "false");
            startService(PORT, "dev").join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Server startService(int port, String profile) {
        Server server = new Server();

//        ServerConnector http = new ServerConnector(server,new HttpConnectionFactory());
//        http.setPort(8080);
//        server.addConnector(http);

        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("keystore");
        sslContextFactory.setKeyStorePassword("aloitepalvelu");

        HttpConfiguration https_config = new HttpConfiguration();
        https_config.addCustomizer(new SecureRequestCustomizer());

        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory,"http/1.1"),
                new HttpConnectionFactory(https_config));
        sslConnector.setPort(port);
        server.addConnector(sslConnector);

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
    
}
