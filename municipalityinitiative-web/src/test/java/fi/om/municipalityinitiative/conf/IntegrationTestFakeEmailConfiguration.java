package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(IntegrationTestConfiguration.class)
public class IntegrationTestFakeEmailConfiguration {


    public static final String EMAIL_DEFAULT_REPLY_TO = "reply_to@example.com";
    
    public static final String EMAIL_DEFAULT_OM = "om@example.com";

    private String googleMapsApiKey = "AIzaSyB7ZfM7ihPAIP586CN0AZgp5QbF9OAMBeI";

    private boolean googleMapsEnabled = true;

    private boolean superSearchEnabled  = false;

    private boolean videoEnabled = false;

    private boolean followEnabled = false;

    @Bean
    public EnvironmentSettings environmentSettings() {
        return new EnvironmentSettings(EMAIL_DEFAULT_REPLY_TO,
                Maybe.<String>absent(),
                false,
                EMAIL_DEFAULT_OM,
                false,
                false,
                true,
                true,
                googleMapsApiKey,
                googleMapsEnabled,
                superSearchEnabled,
                videoEnabled,
                followEnabled);
    }

    @Bean
    public JavaMailSenderFake javaMailSender() {
        return new JavaMailSenderFake();
    }


}
