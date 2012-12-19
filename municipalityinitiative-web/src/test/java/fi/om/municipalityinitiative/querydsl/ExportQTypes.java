package fi.om.municipalityinitiative.querydsl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.codegen.DefaultNamingStrategy;
import com.mysema.query.sql.codegen.MetaDataExporter;

import fi.om.municipalityinitiative.conf.JdbcConfiguration;

public class ExportQTypes {

    @org.springframework.context.annotation.Configuration
    @Import(JdbcConfiguration.class)
    @PropertySource("classpath:test.properties")
    public static class StandaloneJdbcConfiguration {
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(StandaloneJdbcConfiguration.class);
        DataSource dataSource = ctx.getBean(DataSource.class);
        Configuration configuration = ctx.getBean(Configuration.class);

        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("fi.om.municipalityinitiative.sql");
        exporter.setSchemaPattern("municipalityinitiative");
        exporter.setInnerClassesForKeys(false);
        exporter.setNamePrefix("Q");
        exporter.setNamingStrategy(new DefaultNamingStrategy());
        exporter.setTargetFolder(new File("src/main/java"));
        exporter.setConfiguration(configuration);

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            exporter.export(conn.getMetaData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {}
            }
        }
    }
    
}
