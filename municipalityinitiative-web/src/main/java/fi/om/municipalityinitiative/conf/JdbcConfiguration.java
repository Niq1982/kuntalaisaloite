package fi.om.municipalityinitiative.conf;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import com.mysema.query.sql.JavaTypeMapping;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.sql.types.DateTimeType;
import com.mysema.query.sql.types.EnumAsObjectType;
import com.mysema.query.sql.types.EnumByNameType;
import com.mysema.query.sql.types.LocalDateType;
import com.mysema.query.types.Ops;
import fi.om.municipalityinitiative.service.email.EmailReportType;
import fi.om.municipalityinitiative.util.*;
import org.apache.commons.io.FileUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Member;
import java.sql.Connection;

@Configuration
public class JdbcConfiguration {

    private final Logger log = LoggerFactory.getLogger(JdbcConfiguration.class);

    @Inject Environment env;

    @Bean
    public SQLTemplates templates() {
        return new PostgresTemplates() {{
            // https://github.com/mysema/querydsl/pull/280
            setPrintSchema(false);
            add(Ops.DateTimeOps.CURRENT_TIMESTAMP, "current_timestamp");
            add(Ops.DateTimeOps.CURRENT_TIME, "current_time");
            add(Ops.DateTimeOps.CURRENT_DATE, "current_date");
        }};
    }

    /**
     * Default connection pool settings are not defined.
     *
     * Deployment specific overrides in <tt>jar-file-location/config/bonecp-default-config.xml</tt>
     * @return
     */
    @Bean
    public DataSource dataSource() throws IOException {
        return dataSourceWithUser(PropertyNames.jdbcUser, PropertyNames.jdbcPassword);
    }

    private DataSource dataSourceWithUser(String jdbcUser, String jdbcPassword) {
        BoneCPDataSource dataSource;
        try {
            File file = ConfigurationFileLoader.getFile("bonecp-config.xml");
            log.info("Using bonecp-config: " + file.getAbsolutePath());
            try (FileInputStream xmlConfigFile = FileUtils.openInputStream(file)) {
                dataSource = new BoneCPDataSource(new BoneCPConfig(xmlConfigFile, null));
            }
        } catch (Exception e) {
            dataSource = new BoneCPDataSource();
            log.error("Unable to initialize bonecp-config.xml. Using default bonecp-settings.", e);
        }
        dataSource.setDriverClass(env.getRequiredProperty(PropertyNames.jdbcDriver));

        dataSource.setJdbcUrl(env.getRequiredProperty(PropertyNames.jdbcURL));
        dataSource.setUsername(env.getRequiredProperty(jdbcUser));
        dataSource.setPassword(env.getRequiredProperty(jdbcPassword));
        log.info(dataSource.getConfig().getConfigFile());
        log.info(dataSource.toString());

        return dataSource;
    }

    @Bean
    public com.mysema.query.sql.Configuration querydslConfiguration() {
        com.mysema.query.sql.Configuration configuration = new com.mysema.query.sql.Configuration(templates());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());
        configuration.register("municipality_initiative", "type", new EnumAsObjectType<>(InitiativeType.class));
        configuration.register("municipality_initiative", "state", new EnumAsObjectType<>(InitiativeState.class));
        configuration.register("municipality_initiative", "last_email_report_type", new EnumAsObjectType<>(EmailReportType.class));
        configuration.register("participant", "membership_type", new EnumAsObjectType<>(Membership.class));
        configuration.register("verified_participant", "membership_type", new EnumAsObjectType<>(Membership.class));
        configuration.register("municipality_initiative", "fix_state", new EnumAsObjectType<>(FixState.class));
        configuration.register("info_text", "category", new EnumAsObjectType<>(InfoTextCategory.class));
        configuration.register("info_text", "languagecode", new EnumAsObjectType<>(LanguageCode.class));
        configuration.register("email", "attachment", new EnumAsObjectType<>(EmailAttachmentType.class));
        configuration.register("review_history", "type", new EnumAsObjectType<>(ReviewHistoryType.class));

//         configuration.register(new EnumByNameType<>(Membership.class));
        return configuration;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws IOException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public PostgresQueryFactory queryFactory() throws IOException {
        final DataSource dataSource = dataSource();
        return new PostgresQueryFactory(querydslConfiguration(), new Provider<Connection>() {

            @Override
            public Connection get() {
                Connection conn = DataSourceUtils.getConnection(dataSource);
                if (!DataSourceUtils.isConnectionTransactional(conn, dataSource)) {
                    throw new RuntimeException("Connection is not transactional");
                }
                return conn;
            }

        });
    }

    @PostConstruct
    public void updateDatabase() throws IOException {
        try {
            Flyway flyway = new Flyway();
            flyway.setEncoding("UTF-8");
            flyway.setTable("flyway_schema");
            flyway.setLocations("db/migration");
            flyway.setSchemas(env.getProperty(PropertyNames.jdbcUser));
            flyway.setDataSource(
                    env.getProperty(PropertyNames.jdbcURL),
                    env.getProperty(PropertyNames.flywayUser),
                    env.getProperty(PropertyNames.flywayPassword));

            flyway.setBaselineOnMigrate(true);
            flyway.migrate();
        } catch (Exception e) {
            log.error("FAILED TO MIGRATE DATABASE", e);
        }
    }

}
