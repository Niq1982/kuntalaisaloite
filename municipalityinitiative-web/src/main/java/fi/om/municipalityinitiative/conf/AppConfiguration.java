package fi.om.municipalityinitiative.conf;


import com.google.common.collect.Maps;
import com.mysema.query.sql.postgres.PostgresQueryFactory;
import fi.om.municipalityinitiative.conf.AppConfiguration.AppDevConfiguration;
import fi.om.municipalityinitiative.conf.AppConfiguration.ProdPropertiesConfiguration;
import fi.om.municipalityinitiative.conf.AppConfiguration.TestPropertiesConfigurer;
import fi.om.municipalityinitiative.dao.SQLExceptionTranslatorAspect;
import fi.om.municipalityinitiative.newdao.*;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.util.TaskExecutorAspect;
import fi.om.municipalityinitiative.validation.LocalValidatorFactoryBeanFix;
import fi.om.municipalityinitiative.web.CacheHeaderFilter;
import fi.om.municipalityinitiative.web.ErrorFilter;
import fi.om.municipalityinitiative.web.SecurityFilter;
import fi.om.municipalityinitiative.web.Urls;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.utility.XmlEscape;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.inject.Inject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableTransactionManagement(proxyTargetClass=false)
@EnableAspectJAutoProxy(proxyTargetClass=false)
@Import({ProdPropertiesConfiguration.class, TestPropertiesConfigurer.class, JdbcConfiguration.class, AppDevConfiguration.class})
public class AppConfiguration {

    @Inject Environment env;
    
    @Resource JdbcConfiguration jdbcConfiguration; 

    //default values for test properties:
    private static final boolean TEST_EMAIL_CONSOLE_OUTPUT_DEFAULT = false;
    private static final int TEST_MESSAGE_SOURCE_CACHE_SECONDS_DEFAULT = -1;
    private static final boolean TEST_FREEMARKER_SHOW_ERRORS_ON_PAGE_DEFAULT = false;
    
    private PeriodFormatter periodFormatter = ISOPeriodFormat.standard();
    
    /**
     * PRODUCTION PROPERTIES CONFIGURATION: encrypted app.properties
     */
    @Configuration
    @Profile({"prod", "dev"})
    @PropertySource({"classpath:default.properties"})
    public static class ProdPropertiesConfiguration {

        @Bean
        public static EncryptablePropertiesConfigurer propertyProcessor() {
            return new EncryptablePropertiesConfigurer(new ClassPathResource("app.properties"));
        }
        
    }
    
    /**
     * TEST PROPERTIES CONFIGURATION: test.properties
     */
    @Configuration
    @Profile({"test"})
    @PropertySource({"classpath:default.properties", "classpath:test.properties"}) 
    // NOTE: default.properties has to be here, in AppConfiguration level default.properties would override test.properties
    public static class TestPropertiesConfigurer {
        
    }
    
    @Configuration
    @Profile({"dev", "test"})
    public static class AppDevConfiguration {


    }
    
    
    /*
     * BEANS
     */
    
    private PostgresQueryFactory queryFactory() {
        return jdbcConfiguration.queryFactory();
    }

    @Bean
    public MunicipalityInitiativeDao municipalityInitiativeDao() {
        return new JdbcMunicipalityInitiativeDao();
    }

    @Bean
    public MunicipalityDao municipalityDao() {
        return new JdbcMunicipalityDao();
    }

    @Bean
    public ParticipantDao composerDao() {
        return new JdbcParticipantDao();
    }

    @Bean
    public MunicipalityInitiativeService municipalityInitiativeService() {
        return new MunicipalityInitiativeService();
    }

    @Bean
    public MunicipalityService municipalityService() {
        return new MunicipalityService();
    }

    @Bean
    public StatusService statusService() {
        String testEmailSendTo = env.getProperty(PropertyNames.testEmailSendTo);
        boolean testEmailConsoleOutput = env.getProperty(PropertyNames.testEmailConsoleOutput, Boolean.class, TEST_EMAIL_CONSOLE_OUTPUT_DEFAULT);
        int messageSourceCacheSeconds = env.getProperty(PropertyNames.testMessageSourceCacheSeconds, Integer.class, TEST_MESSAGE_SOURCE_CACHE_SECONDS_DEFAULT);
        boolean testFreemarkerShowErrorsOnPage = env.getProperty(PropertyNames.testFreemarkerShowErrorsOnPage, Boolean.class, TEST_FREEMARKER_SHOW_ERRORS_ON_PAGE_DEFAULT);

        return new StatusServiceImpl(testEmailSendTo,
                testEmailConsoleOutput, messageSourceCacheSeconds, testFreemarkerShowErrorsOnPage,
                WebConfiguration.optimizeResources(env),
                WebConfiguration.resourcesVersion(env),
                WebConfiguration.appVersion(env));
    }

    @Bean
    public ValidationService validationService() {
        return new ValidationServiceImpl();
    }

    @Bean
    public EncryptionService encryptionService() {
        return new EncryptionService(
                env.getRequiredProperty(PropertyNames.registeredUserSecret),
                env.getProperty(PropertyNames.vetumaSharedSecret)
            );
    }
    
    @Bean
    public XmlEscape fmXmlEscape() {
        return new XmlEscape();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("WEB-INF/messages", "WEB-INF/m");
        messageSource.setCacheSeconds(env.getProperty(PropertyNames.testMessageSourceCacheSeconds, Integer.class, TEST_MESSAGE_SOURCE_CACHE_SECONDS_DEFAULT));
        return messageSource;
    }
    
    @Bean 
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer config = new FreeMarkerConfigurer();
        config.setDefaultEncoding("UTF-8");
        String[] paths = {"/WEB-INF/freemarker/", "classpath:/freemarker/"}; 
        config.setTemplateLoaderPaths(paths);
        
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("xml_escape", fmXmlEscape());

        config.setFreemarkerVariables(variables);
        return config;
    }
    
    @Bean
    public BeansWrapper freemarkerObjectWrapper() {
        boolean testFreemarkerShowErrorsOnPage = env.getProperty(PropertyNames.testFreemarkerShowErrorsOnPage, Boolean.class, TEST_FREEMARKER_SHOW_ERRORS_ON_PAGE_DEFAULT);
        if (!testFreemarkerShowErrorsOnPage) {
            freemarkerConfig().getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } 
        return (BeansWrapper) freemarkerConfig().getConfiguration().getObjectWrapper();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        String smtpServer = env.getRequiredProperty(PropertyNames.emailSmtpServer);
        Integer smtpServerPort = env.getProperty(PropertyNames.emailSmtpServerPort, Integer.class, null);
        
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(smtpServer);
        if (smtpServerPort != null) { // otherwise use default port
            sender.setPort(smtpServerPort);
        }
        return sender;
    }

    private Period getRequiredPeriod(String key) {
        return periodFormatter.parsePeriod(env.getRequiredProperty(key));
    }
    
    @Bean
    public EmailService emailService() {
        Urls.initUrls(env.getRequiredProperty(PropertyNames.baseURL)); //FIXME: quick hack, move this to right place!!!
        
        String baseURL = env.getRequiredProperty(PropertyNames.baseURL);
        String defaultReplyTo = env.getRequiredProperty(PropertyNames.emailDefaultReplyTo);
        String sendToOM = env.getRequiredProperty(PropertyNames.emailSendToOM);
        String sendToVRK = env.getRequiredProperty(PropertyNames.emailSendToVRK);
        int invitationExpirationDays = env.getRequiredProperty(PropertyNames.invitationExpirationDays, Integer.class);

        String testSendTo = env.getProperty(PropertyNames.testEmailSendTo);
        boolean testConsoleOutput = env.getProperty(PropertyNames.testEmailConsoleOutput, Boolean.class, TEST_EMAIL_CONSOLE_OUTPUT_DEFAULT);
        return new EmailServiceImpl(freemarkerConfig(), messageSource(), javaMailSender(), baseURL, defaultReplyTo, sendToOM, sendToVRK, invitationExpirationDays, testSendTo, testConsoleOutput);
    }
    
    @Bean
    public LocalValidatorFactoryBeanFix validator() {
        return new LocalValidatorFactoryBeanFix();
    }
    
    @Bean
    public SQLExceptionTranslator sqlExceptionTranslator() {
        return new SQLErrorCodeSQLExceptionTranslator(jdbcConfiguration.dataSource());
    }
    
    @Bean
    public SQLExceptionTranslatorAspect sqlExceptionTranslatorAspect() {
        return new SQLExceptionTranslatorAspect(sqlExceptionTranslator());
    }
    
    @Bean 
    public TaskExecutorAspect taskExecutorAspect() {
        return new TaskExecutorAspect();
    }
    
    @Bean
    public ExecutorService executorService() {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run(){
                executorService.shutdown();
            }
         });
        return executorService;
    }
    
    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }
    
    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter(env.getRequiredProperty(PropertyNames.errorFeedbackEmail));
    }

    @Bean
    public CacheHeaderFilter resourceCacheFilter() {
        return new CacheHeaderFilter(WebConfiguration.optimizeResources(env));
    }

    @Bean
    public CacheHeaderFilter noCacheFilter() {
        return new CacheHeaderFilter(WebConfiguration.optimizeResources(env), 0);
    }

    @Bean
    public CacheHeaderFilter apiFilter() {
        return new CacheHeaderFilter(WebConfiguration.optimizeResources(env), 5);
    }
}
