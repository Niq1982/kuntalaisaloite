package fi.om.municipalityinitiative.server;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.util.Log4jConfigListener;

public class JettyServer {

    public static class JettyProperties {
        public final int jettyPort;
        public final int jettyThreadPoolCount;
        public final String springProfile;
        public final String log4jConfigPath;

        public JettyProperties(int jettyPort, int jettyThreadPoolCount,
                               String springProfile, String log4jConfigPath) {
            this.jettyPort = jettyPort;
            this.jettyThreadPoolCount = jettyThreadPoolCount;
            this.springProfile = springProfile;
            this.log4jConfigPath = log4jConfigPath;
        }
    }

    public static Server start(JettyProperties properties) throws Throwable {
//            PropertyConfigurator.configure(properties.log4jConfigPath);


        LoggerFactory.getLogger(JettyServer.class).info("NYT ALETAAN LUOMAAN SERVERII");

        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(properties.jettyThreadPoolCount);

        Server server = new Server(threadPool);

//        SslContextFactory sslContextFactory = new SslContextFactory();
//        sslContextFactory.setKeyStorePath("keystore");
//        sslContextFactory.setKeyStorePassword("aloitepalvelu");
//
//        HttpConfiguration https_config = new HttpConfiguration();
//        https_config.addCustomizer(new SecureRequestCustomizer());
//
//        ServerConnector sslConnector = new ServerConnector(server,
//                new SslConnectionFactory(sslContextFactory,"http/1.1"),
//                new HttpConnectionFactory(https_config));
//        sslConnector.setPort(properties.jettyPort);
//        server.addConnector(sslConnector);

        ServerConnector http = new ServerConnector(server,new HttpConnectionFactory());
        http.setPort(properties.jettyPort);
        server.addConnector(http);

        WebAppContext context = new WebAppContext();

        // Logging configured per environment:
        context.addEventListener(new Log4jConfigListener());
        context.setInitParameter("log4jConfigLocation", "file:" + properties.log4jConfigPath);
        context.setInitParameter("log4jExposeWebAppRoot", "false");

        context.setDescriptor(new ClassPathResource("src/main/webapp/WEB-INF/web.xml").getURI().toString());
        context.setResourceBase(new ClassPathResource("src/main/webapp").getURI().toString());

        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false"); // TODO: TRUE ? FALSE ?

        context.setInitParameter("spring.profiles.active", properties.springProfile);


//        ResourceHandler resourceHandler = new ResourceHandler();
//
//        // The directory where the src/main/webapp resources reside in the JAR:
//        resourceHandler.setBaseResource(Resource.newClassPathResource("src/main/webapp"));
//        resourceHandler.setDirectoriesListed(false);
//        resourceHandler.setEtags(true);
////        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
//
//        context.setHandler(resourceHandler);

        server.setHandler(context);
        server.start();
        System.out.println("Joining server");
        server.join();
        return server;
    }


}
