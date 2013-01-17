package fi.om.municipalityinitiative.conf;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.DatabindVersion;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import fi.om.municipalityinitiative.conf.WebConfiguration.WebDevConfiguration;
import fi.om.municipalityinitiative.conf.WebConfiguration.WebProdConfiguration;
import fi.om.municipalityinitiative.json.JsonIdAnnotationIntrospector;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeCreateController;
import fi.om.municipalityinitiative.newweb.MunicipalityInitiativeViewController;
import fi.om.municipalityinitiative.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.inject.Inject;

import java.util.List;
import java.util.Locale;


@Configuration
//@EnableWebMvc -- replaced by "extends WebMvcConfigurationSupport"
@Import({ WebProdConfiguration.class, WebDevConfiguration.class })
//@ComponentScan(basePackageClasses=InitiativeController.class)
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Inject Environment env;

    /**
     * DEVELOPMENT AND TEST WEB CONTROLLERS
     */
    @Configuration
    @Profile({"dev", "test"})
    public static class WebDevConfiguration {

        @Inject Environment env;

    }

    public static Boolean optimizeResources(Environment env) {
        return env.getProperty(PropertyNames.optimizeResources, Boolean.class, true);
    }

    public static String resourcesVersion(Environment env) {
        return env.getProperty(PropertyNames.resourcesVersion, "dev");
    }

    public static String appVersion(Environment env) {
        return env.getProperty(PropertyNames.appVersion, "<no version>");
    }

    public static Optional<Integer> omPiwicId(Environment env) {
        String piwicId = env.getProperty(PropertyNames.omPiwicId);
        if (Strings.isNullOrEmpty(piwicId)) {
            return Optional.absent();
        }
        else {
            return Optional.of(Integer.valueOf(piwicId));
        }
    }

    /**
     * PRODUCTION WEB CONTROLLERS
     */
    @Configuration
    @Profile("prod")
    public static class WebProdConfiguration {
    }


    /*
     * BEANS
     */

    @Bean
    public LocaleResolver localeResolver() {
        return new URILocaleResolver();
    }

    @Bean
    public ViewResolver viewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver() {
            /**
             * Override redirect view handling: do not expose model attributes!
             */
            @Override
            protected View createView(String viewName, Locale locale) throws Exception {
                // If this resolver is not supposed to handle the given view,
                // return null to pass on to the next resolver in the chain.
                if (!canHandle(viewName, locale)) {
                    return null;
                }
                // Check for special "redirect:" prefix.
                if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
                    String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
                    boolean exposeModelAttributes = false;
                    return new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible(), exposeModelAttributes);
                }
                // Check for special "forward:" prefix.
                if (viewName.startsWith(FORWARD_URL_PREFIX)) {
                    String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
                    return new InternalResourceView(forwardUrl);
                }

                return loadView(viewName, locale);
            }
        };

        viewResolver.setCache(false);
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".ftl");
        viewResolver.setExposeSpringMacroHelpers(true);
        viewResolver.setExposeRequestAttributes(false);
        viewResolver.setExposeSessionAttributes(false);
        viewResolver.setContentType("text/html;charset=UTF-8");

        return viewResolver;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);

        converters.add(jsonConverter());
        converters.add(jsonpConverter());
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringConverter);
    }

    @Bean
    public HttpMessageConverter<?> jsonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Module() {

            @Override
            public String getModuleName() {
                return "JsonIdHandler";
            }

            @Override
            public Version version() {
                return DatabindVersion.instance.version();
            }

            @Override
            public void setupModule(SetupContext context) {
                context.appendAnnotationIntrospector(
                        new JsonIdAnnotationIntrospector(env.getRequiredProperty(PropertyNames.baseURL)));
            }

        });

        converter.setObjectMapper(objectMapper);

        return converter;
    }

    @Bean
    public HttpMessageConverter<?> jsonpConverter() {
        return new JsonpMessageConverter<>(jsonConverter());
    }


    @Bean
    public MunicipalityInitiativeCreateController municipalityInitiativeCreateController() {
        return new MunicipalityInitiativeCreateController(optimizeResources(env), resourcesVersion(env));
    }

    @Bean
    public MunicipalityInitiativeViewController municipalityInitiativeViewController() {
        return new MunicipalityInitiativeViewController(optimizeResources(env), resourcesVersion(env));
    }
    
    @Bean
    public StaticPageController staticPageController() {
        return new StaticPageController(
                optimizeResources(env),
                resourcesVersion(env),
                omPiwicId(env));
    }

    @Bean
    public StatusPageController statusPageController() {
        return new StatusPageController(
                optimizeResources(env),
                resourcesVersion(env
                )
        );
    }

    @Bean
    public ErrorController errorController() {
        return new ErrorController();
    }

}
