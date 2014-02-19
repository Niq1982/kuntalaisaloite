package fi.om.municipalityinitiative.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.LoggerFactory;
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

        Server server = new Server(properties.jettyPort);
        // TODO: server.setThreadPool(new QueuedThreadPool(properties.jettyThreadPoolCount));

        WebAppContext context = new WebAppContext();

        // Logging configured per environment:
        context.addEventListener(new Log4jConfigListener());
        context.setInitParameter("log4jConfigLocation", "file:" + properties.log4jConfigPath);
        context.setInitParameter("log4jExposeWebAppRoot", "false");

        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp/");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false"); // TODO: TRUE ? FALSE ?

        context.setInitParameter("spring.profiles.active", properties.springProfile);

        server.setHandler(context);
        server.start();
        System.out.println("Joining server");
        server.join();
        return server;
    }


}
