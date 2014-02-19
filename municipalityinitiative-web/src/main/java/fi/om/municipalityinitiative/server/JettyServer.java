package fi.om.municipalityinitiative.server;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

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

    public static void start(JettyProperties properties) throws Throwable {
            PropertyConfigurator.configure(properties.log4jConfigPath);
            Server server = new Server(properties.jettyPort);
            server.setThreadPool(new QueuedThreadPool(properties.jettyThreadPoolCount));

            WebAppContext context = new WebAppContext();
            context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
            context.setResourceBase("src/main/webapp/");
            context.setContextPath("/");
            context.setParentLoaderPriority(true);
            context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false"); // TODO: TRUE ? FALSE ?

            context.setInitParameter("spring.profiles.active", properties.springProfile);

            server.setHandler(context);
            server.start();
            server.join();
    }


}
