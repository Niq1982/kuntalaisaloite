package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.dao.NEWTestHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import(AppConfiguration.class)
@PropertySource({"classpath:default.properties", "classpath:test.properties"})
@EnableTransactionManagement(proxyTargetClass=false)
public class NEWIntegrationTestConfiguration {

    @Bean
    public NEWTestHelper testHelper() {
        return new NEWTestHelper();
    }
}

