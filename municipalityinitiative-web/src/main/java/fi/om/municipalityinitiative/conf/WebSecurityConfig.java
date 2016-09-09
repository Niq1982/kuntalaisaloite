package fi.om.municipalityinitiative.conf;

import com.google.common.collect.Maps;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.saml2.metadata.provider.ResourceBackedMetadataProvider;
import org.opensaml.util.resource.ClasspathResource;
import org.opensaml.util.resource.ResourceException;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.*;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.EmptyKeyManager;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.*;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.httpclient.TLSProtocolConfigurer;
import org.springframework.security.saml.trust.httpclient.TLSProtocolSocketFactory;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;

/**
 * "Forked" from https://github.com/vdenotaris/spring-boot-security-saml-sample
 * Thanks Vincenzo !
 */
@Configuration
@EnableWebSecurity
@ImportResource("classpath:samlConfig.xml")
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    Environment environment;

    private SAMLUserDetailsService samlUserDetailsService = new SAMLUserDetailsService() {

        @Override
        public Object loadUserBySAML(SAMLCredential samlCredential) throws UsernameNotFoundException {
            return null;
        }
    };

    // Initialization of the velocity engine
    @Bean
    public VelocityEngine velocityEngine() {
        return VelocityFactory.getEngine();
    }

    // XML parser pool needed for OpenSAML parsing
    @Bean(initMethod = "initialize")
    public StaticBasicParserPool parserPool() {
        StaticBasicParserPool staticBasicParserPool = new StaticBasicParserPool();

        HashMap<String, Boolean> newFeatures = Maps.newHashMap();
        newFeatures.put("http://apache.org/xml/features/dom/defer-node-expansion", false);
        staticBasicParserPool.setBuilderFeatures(newFeatures);
        return staticBasicParserPool;
    }

    @Bean(name = "parserPoolHolder")
    public ParserPoolHolder parserPoolHolder() {
        return new ParserPoolHolder();
    }

    // Bindings, encoders and decoders used for creating and parsing messages
    @Bean
    public MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager() {
        return new MultiThreadedHttpConnectionManager();
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient(multiThreadedHttpConnectionManager());
    }

    // SAML Authentication Provider responsible for validating of received SAML
    // messages
    @Bean
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
        samlAuthenticationProvider.setUserDetails(samlUserDetailsService);
        samlAuthenticationProvider.setForcePrincipalAsString(false);
        return samlAuthenticationProvider;
    }

    // Provider of default SAML Context
    @Bean
    public SAMLContextProviderImpl contextProvider() throws MalformedURLException {
        SAMLContextProviderLB samlContextProviderLB = new SAMLContextProviderLB();

        // This is here because apparently spring saml expects ../saml/SSO request to use http if server is answering from http
        URL url = new URL(environment.getProperty("app.baseURL"));

        samlContextProviderLB.setScheme(url.getProtocol());
        samlContextProviderLB.setServerName(url.getHost());
        samlContextProviderLB.setContextPath(url.getPath());
        return samlContextProviderLB;
    }

    // Initialization of OpenSAML library
    @Bean
    public static SAMLBootstrap sAMLBootstrap() {
        return new SAMLBootstrap();
    }

    // Logger for SAML messages and events
    @Bean
    public SAMLDefaultLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    // SAML 2.0 WebSSO Assertion Consumer
    @Bean
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }

    // SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 Web SSO profile
    @Bean
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    // SAML 2.0 Holder-of-Key Web SSO profile
    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    // SAML 2.0 ECP profile
    @Bean
    public WebSSOProfileECPImpl ecpprofile() {
        return new WebSSOProfileECPImpl();
    }

    @Bean
    public SingleLogoutProfile logoutprofile() {
        return new SingleLogoutProfileImpl();
    }

    // Central storage of cryptographic keys
    @Bean
    public KeyManager keyManager() {

        try {
            Resource storeFile = new FileSystemResourceLoader().getResource(environment.getProperty("keystore.location"));


//            if (Arrays.stream(environment.getActiveProfiles()).anyMatch("saml"::equals)) {
            if (Boolean.valueOf(environment.getProperty("saml.enabled"))) {
                String keystoreKey = environment.getProperty("keystore.key");
                String storePass = environment.getProperty("keystore.password");

                Map<String, String> passwords = new HashMap<>();

                passwords.put(keystoreKey, environment.getProperty("keystore.key.password"));

                KeyStore jceks = KeyStore.getInstance("JCEKS");

                jceks.load(storeFile.getInputStream(), storePass.toCharArray());
                return new JKSKeyManager(storeFile, storePass, passwords, keystoreKey);
            } else {
                return new EmptyKeyManager();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Setup TLS Socket Factory
    @Bean
    public TLSProtocolConfigurer tlsProtocolConfigurer() {
        return new TLSProtocolConfigurer();
    }

    @Bean
    public ProtocolSocketFactory socketFactory() {
        return new TLSProtocolSocketFactory(keyManager(), null, "default");
    }

    @Bean
    public Protocol socketFactoryProtocol() {
        return new Protocol("https", socketFactory(), 443);
    }

    @Bean
    public MethodInvokingFactoryBean socketFactoryInitialization() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(Protocol.class);
        methodInvokingFactoryBean.setTargetMethod("registerProtocol");
        Object[] args = {"https", socketFactoryProtocol()};
        methodInvokingFactoryBean.setArguments(args);
        return methodInvokingFactoryBean;
    }

    @Bean
    public WebSSOProfileOptions defaultWebSSOProfileOptions() {
        WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
        webSSOProfileOptions.setIncludeScoping(false);
        return webSSOProfileOptions;
    }

    // Entry point to initialize authentication, default values taken from
    // properties file
    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
        samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
        return samlEntryPoint;
    }

    // Setup advanced info about metadata
    @Bean
    public ExtendedMetadata extendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setIdpDiscoveryEnabled(false);
        extendedMetadata.setSignMetadata(false);

        return extendedMetadata;
    }

    // IDP Discovery Service
//    @Bean
//    public SAMLDiscovery samlIDPDiscovery() {
//        SAMLDiscovery idpDiscovery = new SAMLDiscovery();
//        idpDiscovery.setIdpSelectionPath("/saml/idpSelection");
//        return idpDiscovery;
//    }

//    @Bean
//    @Qualifier("testshib")
//    public ExtendedMetadataDelegate ssoCircleExtendedMetadataProvider()
//            throws MetadataProviderException {
//        String idpSSOCircleMetadataURL = "http://www.testshib.org/metadata/testshib-providers.xml";
//        Timer backgroundTaskTimer = new Timer(true);
//        HTTPMetadataProvider httpMetadataProvider = new HTTPMetadataProvider(
//                backgroundTaskTimer, httpClient(), idpSSOCircleMetadataURL);
//        httpMetadataProvider.setParserPool(parserPool());
//        ExtendedMetadataDelegate extendedMetadataDelegate =
//                new ExtendedMetadataDelegate(httpMetadataProvider, extendedMetadata());
//        extendedMetadataDelegate.setMetadataTrustCheck(true);
//        extendedMetadataDelegate.setMetadataRequireSignature(false);
//        return extendedMetadataDelegate;
//    }

    @Bean
    public ExtendedMetadataDelegate kapaExtendedMetadataProvider() throws MetadataProviderException, ResourceException {

        ResourceBackedMetadataProvider metadataProvider = new ResourceBackedMetadataProvider(new Timer(), new ClasspathResource("/saml/testi.apro.idp.xml"));

        metadataProvider.setFailFastInitialization(false);
        metadataProvider.setParserPool(parserPool());

        ExtendedMetadataDelegate extendedMetadataDelegate =
                new ExtendedMetadataDelegate(metadataProvider, extendedMetadata());
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        extendedMetadataDelegate.setMetadataRequireSignature(false);
        return extendedMetadataDelegate;
    }



    // IDP Metadata configuration - paths to metadata of IDPs in circle of trust
    // is here
    // Do no forget to call iniitalize method on providers
    @Bean
    @Qualifier("metadata")
    public CachingMetadataManager metadata() throws Exception {
        List<MetadataProvider> providers = new ArrayList<MetadataProvider>();
        providers.add(kapaExtendedMetadataProvider());


        ExtendedMetadata defaultMetadata = new ExtendedMetadata();
        defaultMetadata.setLocal(true);
//        defaultMetadata.setSignMetadata(true);

        FileTemplateMetadataProvider delegate = new FileTemplateMetadataProvider(new ClassPathResource("saml/our-metadata.xml"),
                environment.getProperty("jks.public.key"),
                environment.getProperty("app.baseURL"));
        delegate.setParserPool(parserPool());

        ExtendedMetadataDelegate localMetadataDelegate = new ExtendedMetadataDelegate(delegate, defaultMetadata);
        providers.add(localMetadataDelegate);
        return new CachingMetadataManager(providers);
    }

    // Filter automatically generates default SP metadata
    @Bean
    public MetadataGenerator metadataGenerator() {
        MetadataGenerator metadataGenerator = new MetadataGenerator();
        metadataGenerator.setExtendedMetadata(extendedMetadata());
        metadataGenerator.setIncludeDiscoveryExtension(false);
        metadataGenerator.setKeyManager(keyManager());
        metadataGenerator.setEntityBaseURL(environment.getProperty("app.baseURL"));

        return metadataGenerator;
    }

    // The filter is waiting for connections on URL suffixed with filterSuffix
    // and presents SP metadata there
    @Bean
    public MetadataDisplayFilter metadataDisplayFilter() {
        return new MetadataDisplayFilter();
    }

    // Handler deciding where to redirect user after successful login
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        successRedirectHandler.setDefaultTargetUrl(appURI("/landing"));
        return successRedirectHandler;
    }

    // Handler deciding where to redirect user after failed login
    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler =
                new SimpleUrlAuthenticationFailureHandler();
        failureHandler.setUseForward(true);
        failureHandler.setDefaultFailureUrl(appURI("/error"));
        return failureHandler;
    }

//    @Bean
//    public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
//        SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
//        samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
//        samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
//        samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
//        return samlWebSSOHoKProcessingFilter;
//    }
//
    // Processing filter for WebSSO profile messages
    @Bean
    public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
        SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
        samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
        samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
        samlWebSSOProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return samlWebSSOProcessingFilter;
    }

    @Bean
    public MetadataGeneratorFilter metadataGeneratorFilter() {
        return new MetadataGeneratorFilter(metadataGenerator());
    }

    // Handler for successful logout
    @Bean
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        successLogoutHandler.setDefaultTargetUrl(appURI("/logout"));
        return successLogoutHandler;
    }

    private String appURI(String s) {
        return environment.getProperty("app.baseURL") + s;
    }

    // Logout handler terminating local session
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler =
                new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);
        return logoutHandler;
    }

    // Filter processing incoming logout messages
    // First argument determines URL user will be redirected to after successful
    // global logout
    @Bean
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(),
                logoutHandler());
    }

    // Overrides default logout processing filter with the one processing SAML
    // messages
    @Bean
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(),
                new LogoutHandler[] { logoutHandler() },
                new LogoutHandler[] { logoutHandler() });
    }

    // Bindings
//    private ArtifactResolutionProfile artifactResolutionProfile() {
//        final ArtifactResolutionProfileImpl artifactResolutionProfile =
//                new ArtifactResolutionProfileImpl(httpClient());
//        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
//        return artifactResolutionProfile;
//    }

//    @Bean
//    public HTTPArtifactBinding artifactBinding(ParserPool parserPool, VelocityEngine velocityEngine) {
//        return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
//    }

//    @Bean
//    public HTTPSOAP11Binding soapBinding() {
//        return new HTTPSOAP11Binding(parserPool());
//    }

    @Bean
    public HTTPPostBinding httpPostBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    @Bean
    public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

//    @Bean
//    public HTTPSOAP11Binding httpSOAP11Binding() {
//        return new HTTPSOAP11Binding(parserPool());
//    }
//
//    @Bean
//    public HTTPPAOS11Binding httpPAOS11Binding() {
//        return new HTTPPAOS11Binding(parserPool());
//    }

    // Processor
    @Bean
    public SAMLProcessorImpl processor() {
        Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
        bindings.add(httpRedirectDeflateBinding());
        bindings.add(httpPostBinding());
//        bindings.add(artifactBinding(parserPool(), velocityEngine()));
//        bindings.add(httpSOAP11Binding());
//        bindings.add(httpPAOS11Binding());
        return new SAMLProcessorImpl(bindings);
    }

    /**
     * Define the security filter chain in order to support SSO Auth by using SAML 2.0
     *
     * @return Filter chain proxy
     * @throws Exception
     */
    @Bean
    public FilterChainProxy samlFilter() throws Exception {
        List<SecurityFilterChain> chains = new ArrayList<SecurityFilterChain>();
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"),
                samlEntryPoint()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"),
                samlLogoutFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"),
                metadataDisplayFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"),
                samlWebSSOProcessingFilter()));
//        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"),
//                samlWebSSOHoKProcessingFilter()));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"),
                samlLogoutProcessingFilter()));
        return new FilterChainProxy(chains);
    }

    /**
     * Returns the authentication manager currently used by Spring.
     * It represents a bean definition with the aim allow wiring from
     * other classes performing the Inversion of Control (IoC).
     *
     * @throws  Exception
     */
    @Bean(name= "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Sets a custom authentication provider.
     *
     * @param   auth SecurityBuilder used to create an AuthenticationManager.
     * @throws  Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(samlAuthenticationProvider());
    }

}
