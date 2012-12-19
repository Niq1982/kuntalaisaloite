package fi.om.municipalityinitiative.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(AppConfiguration.class)
@PropertySource({"classpath:default.properties", "classpath:test.properties"})
public class ControllerTestConfiguration {

    // TODO: Instead of importing AppConfiguration, implement own configuration which makes us able to mock all services.

}

