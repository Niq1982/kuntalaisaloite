package fi.om.municipalityinitiative.conf;

import java.sql.Connection;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;

import com.jolbox.bonecp.BoneCPDataSource;
import com.mysema.query.sql.PostgresTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import com.mysema.query.sql.types.DateTimeType;
import com.mysema.query.sql.types.EnumAsObjectType;
import com.mysema.query.sql.types.LocalDateType;
import com.mysema.query.types.Ops;

import fi.om.municipalityinitiative.dto.AuthorRole;
import fi.om.municipalityinitiative.dto.InitiativeState;
import fi.om.municipalityinitiative.dto.LanguageCode;
import fi.om.municipalityinitiative.dto.ProposalType;

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
     * Default connection pool settings are defined in <tt>classpath:/bonecp-default-config.xml</tt>.
     * 
     * Deployment specific overrides in <tt>classpath:/bonecp-config.xml</tt>, e.g. <tt>${jetty.home}/resources/bonecp-config.xml</tt>.
     * @return
     */
    @Bean 
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(env.getRequiredProperty(PropertyNames.jdbcDriver));
        dataSource.setJdbcUrl(env.getRequiredProperty(PropertyNames.jdbcURL));
        dataSource.setUsername(env.getRequiredProperty(PropertyNames.jdbcUser));
        dataSource.setPassword(env.getRequiredProperty(PropertyNames.jdbcPassword));
        
        log.info(dataSource.toString());
        
        return dataSource;
    }
    @Bean
    public com.mysema.query.sql.Configuration querydslConfiguration() {
        com.mysema.query.sql.Configuration configuration = new com.mysema.query.sql.Configuration(templates());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());
        configuration.register("initiative", "proposalType", new EnumAsObjectType<ProposalType>(ProposalType.class));
        configuration.register("initiative", "state", new EnumAsObjectType<InitiativeState>(InitiativeState.class));
        configuration.register("initiative", "primaryLanguage", new EnumAsObjectType<LanguageCode>(LanguageCode.class));
        configuration.register("initiative_author", "role", new EnumAsObjectType<AuthorRole>(AuthorRole.class));
        configuration.register("initiative_invitation", "role", new EnumAsObjectType<AuthorRole>(AuthorRole.class));
        
        return configuration;
    }
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    @Bean
    public PostgresQueryFactory queryFactory() {
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
    
}
