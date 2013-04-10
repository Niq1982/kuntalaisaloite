package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.service.EmailService;
import fi.om.municipalityinitiative.service.MailSendingEmailService;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import fi.om.municipalityinitiative.util.Maybe;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
@Import(IntegrationTestConfiguration.class)
public class IntegrationTestFakeEmailConfiguration {

    // TODO: Double check the need for emailSettings and that everything works correctly after we're sending emails again

    public static final String EMAIL_DEFAULT_REPLY_TO = "reply_to@example.com";
    
    public static final String EMAIL_DEFAULT_OM = "om@example.com";

    @Bean
    public EmailSettings emailSettings() {
        return new EmailSettings(EMAIL_DEFAULT_REPLY_TO, Maybe.<String>absent(), false, EMAIL_DEFAULT_OM);
    }

    @Bean
    public JavaMailSenderFake javaMailSender() {
        return new JavaMailSenderFake();
    }
}
