package fi.om.municipalityinitiative.conf;

import fi.om.municipalityinitiative.dao.TestHelper;
import fi.om.municipalityinitiative.service.EmailService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.File;

@Configuration
@Import(AppConfiguration.class)
@PropertySource({"classpath:default.properties", "classpath:test.properties"})
public class IntegrationTestConfiguration {

    @Bean
    public TestHelper testHelper() {
        return new TestHelper();
    }

    @Bean
    public EmailService emailService() {
        return new EmailService();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        File file = new File(System.getProperty("user.dir"), "src/main/webapp/WEB-INF/messages");
        messageSource.setBasenames(file.toURI().toString());
        return messageSource;
    }

}

