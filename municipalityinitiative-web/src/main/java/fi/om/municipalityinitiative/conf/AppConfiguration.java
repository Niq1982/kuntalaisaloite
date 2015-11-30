package fi.om.municipalityinitiative.conf;


import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import fi.om.municipalityinitiative.conf.AppConfiguration.AppDevConfiguration;
import fi.om.municipalityinitiative.conf.AppConfiguration.ProdPropertiesConfiguration;
import fi.om.municipalityinitiative.conf.AppConfiguration.TestPropertiesConfigurer;
import fi.om.municipalityinitiative.dao.*;
import fi.om.municipalityinitiative.dto.service.TestDataService;
import fi.om.municipalityinitiative.service.*;
import fi.om.municipalityinitiative.service.email.*;
import fi.om.municipalityinitiative.service.ui.*;
import fi.om.municipalityinitiative.util.ImageModifier;
import fi.om.municipalityinitiative.util.Maybe;
import fi.om.municipalityinitiative.util.TaskExecutorAspect;
import fi.om.municipalityinitiative.validation.LocalValidatorFactoryBeanFix;
import fi.om.municipalityinitiative.web.CacheHeaderFilter;
import fi.om.municipalityinitiative.web.ErrorFilter;
import fi.om.municipalityinitiative.web.SecurityFilter;
import fi.om.municipalityinitiative.web.Urls;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.utility.XmlEscape;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
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

import javax.annotation.PostConstruct;
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
    @PropertySource({"classpath:dev.properties"})
    public static class AppDevConfiguration {

        @Bean
        public TestDataService testDataService() {
            return new TestDataService();
        }

    }
    
    
    /*
     * BEANS
     */

//    @Bean
//    public CommonsMultipartResolver multipartResolver() {
//        return new CommonsMultipartResolver();
//    }

    @Bean
    public AuthorMessageDao authorMessageDao() {
        return new JdbcAuthorMessageDao();
    }

    @Bean
    public InfoTextDao infoTextDao() {
        return new JdbcInfoTextDao();
    }

    @Bean
    public UserDao adminUserDao() {
        return new JdbcUserDao();
    }

    @Bean
    public InfoTextService infoTextService() {
        return new InfoTextService();
    }

    @Bean
    public InitiativeDao municipalityInitiativeDao() {
        return new JdbcInitiativeDao();
    }

    @Bean
    public EmailDao emailDao() {
        return new JdbcEmailDao();
    }

    @Bean
    public ReviewHistoryDao reviewHistoryDao() {
        return new JdbcReviewHistoryDao();
    }

    @Bean
    public MunicipalityDao municipalityDao() {
        return new JdbcMunicipalityDao();
    }

    @Bean
    public ParticipantDao participantDao() {
        return new JdbcParticipantDao();
    }

    @Bean
    public AuthorDao authorDao() {
        return new JdbcAuthorDao();
    }

    @Bean
    public JdbcSchemaVersionDao schemaVersionDao() {
        return new JdbcSchemaVersionDao();
    }

    @Bean
    public SupportCountDao supportCountDao() {return new SupportCountDao();}

    @Bean
    public LocationDao locationDao() {return new JdbcLocationDao();}

    @Bean
    public MunicipalityUserDao municipalityUserDao() {return new JdbcMunicipalityUserDao() ;}

    @Bean
    public FollowInitiativeDao followInitiativeDao() {return new JdbcFollowInitiativeDao();}

    @Bean
    public AuthorService authorService() {
        return new AuthorService();
    }

    @Bean
    public NormalInitiativeService normalInitiativeService() {
        return new NormalInitiativeService();
    }

    @Bean
    public PublicInitiativeService publicInitiativeService() {
        return new PublicInitiativeService();
    }

    @Bean
    public YouthInitiativeService youthInitiativeService() { return new YouthInitiativeService(); }

    @Bean
    public InitiativeManagementService initiativeManagementService() {
        return new InitiativeManagementService();
    }

    @Bean
    public ModerationService omInitiativeService() {
        return new ModerationService();
    }

    @Bean
    public JsonDataService jsonDataService() {
        return new JsonDataService();
    }

    @Bean
    public MunicipalityService municipalityService() {
        return new MunicipalityService();
    }

    @Bean
    public MunicipalityUserService municipalityUserService() {
        return new MunicipalityUserService();
    }

    @Bean
    public ParticipantService participantService() {
        return new ParticipantService();
    }

    @Bean
    public LocationService locationService() { return new  LocationService();}

    @Bean
    public FollowInitiativeService followInitiativeService() {return new FollowInitiativeService();}

    @Bean
    public JobExecutor jobExecutor() {
        return new JobExecutor();
    }

    @Bean
    public UserService userService() {
        return new UserService(env.getRequiredProperty(PropertyNames.omUserSalt));
    }

    @Bean
    public MunicipalityDecisionService decisionService() {
        return new MunicipalityDecisionService(env.getRequiredProperty(PropertyNames.decisionAttachmentDir));
    }

    @Bean
    public DecisionAttachmentDao decisionAttachmentDao(){
        return new JdbcDecisionAttachmentDao();
    }

    @Bean
    public ImageFinder imageFinder() {
        return new FileImageFinder(env.getRequiredProperty(PropertyNames.omImageDirection), env.getRequiredProperty(PropertyNames.baseURL));
    }

    @Bean
    public CachedInitiativeFinder cachedInitiativeFinder() {
        return new CachedInitiativeFinder();
    }

    @Bean
    public VerifiedInitiativeService verifiedInitiativeService() {
        return new VerifiedInitiativeService();
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
    public YouthInitiativeWebServiceNotifier youthInitiativeWebServiceNotifier() {
        return new YouthInitiativeWebServiceNotifier(env.getRequiredProperty(PropertyNames.youthInitiativeBaseUrl));
    }

    @Bean
    EmailServiceDataProvider emailServiceDataProvider() {
        return new EmailServiceDataProvider();
    }

    @Bean
    EmailSenderScheduler emailSenderScheduler() {
        return new EmailSenderScheduler();
    }

    @Bean
    EmailSender emailSender() {
        return new EmailSender();
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
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
        ehCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean.getObject());
        return ehCacheCacheManager;
    }
    
    @Bean
    public XmlEscape fmXmlEscape() {
        return new XmlEscape();
    }

    @Bean
    public AttachmentService attachmentService() {
        return new AttachmentService(env.getRequiredProperty(PropertyNames.attachmentDir));
    }

    @Bean
    public AttachmentDao attachmentDao() {
        return new JdbcAttachmentDao();
    }
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("WEB-INF/messages");

        //File file = new File(System.getProperty("user.dir"), "src/main/webapp/WEB-INF/messages");
        //messageSource.setBasenames(file.toURI().toString());

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
    public EmailReportService emailReportService() {
        return new EmailReportService();
    }
    
    @Bean
    public BeansWrapper freemarkerObjectWrapper(FreeMarkerConfigurer configurer) {
        configurer.getConfiguration().setNumberFormat("#");

        boolean testFreemarkerShowErrorsOnPage = env.getProperty(PropertyNames.testFreemarkerShowErrorsOnPage, Boolean.class, TEST_FREEMARKER_SHOW_ERRORS_ON_PAGE_DEFAULT);
        if (!testFreemarkerShowErrorsOnPage) {
            configurer.getConfiguration().setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        } 
        return (BeansWrapper) configurer.getConfiguration().getObjectWrapper();
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

    @Bean
    public EmailService emailService() {
        return new EmailService();
    }

    @Bean
    public SupportCountService supportCountService() {return new SupportCountService();}


    @Bean
    public EnvironmentSettings environmentSettings() {
        String defaultReplyTo = env.getRequiredProperty(PropertyNames.emailDefaultReplyTo);
        String moderatorSendTo = env.getRequiredProperty(PropertyNames.emailSendToOM);
        String testSendTo = env.getProperty(PropertyNames.testEmailSendTo);

        boolean testConsoleOutput = env.getProperty(PropertyNames.testEmailConsoleOutput, Boolean.class, TEST_EMAIL_CONSOLE_OUTPUT_DEFAULT);

        // Mandatory so we won't accidentally leave any test properties to any environments
        boolean testSendMunicipalityEmailsToAuthor = env.getRequiredProperty(PropertyNames.testEmailMunicipalityEmailsToAuthor, Boolean.class);
        boolean testSendModeratorEmailsToAuthor = env.getRequiredProperty(PropertyNames.testEmailSendModeratorEmailsToAuthor, Boolean.class);

        return new EnvironmentSettings(defaultReplyTo,
                Maybe.fromNullable(Strings.emptyToNull(testSendTo)),
                testConsoleOutput,
                moderatorSendTo,
                testSendMunicipalityEmailsToAuthor,
                testSendModeratorEmailsToAuthor,
                Boolean.valueOf(env.getRequiredProperty(PropertyNames.enableVerifiedInitiatives)),
                Boolean.valueOf(env.getRequiredProperty(PropertyNames.isTestEmailSender)),
                String.valueOf(env.getRequiredProperty(PropertyNames.googleMapsApiKey)),
                Boolean.valueOf(env.getRequiredProperty(PropertyNames.googleMapsEnabled)),
                Boolean.valueOf(env.getRequiredProperty(PropertyNames.superSearchEnabled)),
                Boolean.valueOf(env.getProperty(PropertyNames.videoEnabled)));
    }

    @Bean
    public EmailMessageConstructor emailMessageConstructor() {
        return new EmailMessageConstructor();
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
    public VideoService videoService() {
        return new VideoService();
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

    @Bean
    public ImageModifier imageModifier() {
        return new ImageModifier();
    }

    @PostConstruct
    public void initUrls() {
        String baseUrl = env.getRequiredProperty(PropertyNames.baseURL);
        Urls.initUrls(baseUrl,
                env.getProperty(PropertyNames.iframeBaseUrl, baseUrl),
                env.getProperty(PropertyNames.apiBaseUrl, baseUrl),
                env.getRequiredProperty(PropertyNames.youthInitiativeBaseUrl),
                env.getRequiredProperty(PropertyNames.superSearchBaseUrl));
    }
}
