package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.service.EmailService;
import fi.om.municipalityinitiative.service.MailSendingEmailService;
import fi.om.municipalityinitiative.util.JavaMailSenderFake;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
@Import(IntegrationTestConfiguration.class)
public class IntegrationTestFakeEmailConfiguration {

    public static final String EMAIL_DEFAULT_REPLY_TO = "reply_to@example.com";

    @Bean
    public EmailService emailService(FreeMarkerConfigurer freeMarkerConfigurer,
                                     MessageSource messageSource,
                                     JavaMailSenderFake javaMailSender) {

        boolean testConsoleOutput = false;
        String testSendTo = null;
        return new MailSendingEmailService(freeMarkerConfigurer, messageSource, javaMailSender, EMAIL_DEFAULT_REPLY_TO, testSendTo, testConsoleOutput);
    }

    @Bean
    public JavaMailSenderFake javaMailSender() {
        return new JavaMailSenderFake();
    }
}
