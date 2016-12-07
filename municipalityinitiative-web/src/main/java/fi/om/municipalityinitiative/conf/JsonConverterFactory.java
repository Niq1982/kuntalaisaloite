package fi.om.municipalityinitiative.conf;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import fi.om.municipalityinitiative.json.JsonIdAnnotationIntrospector;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class JsonConverterFactory {
    public static MappingJackson2HttpMessageConverter JacksonHttpConverterWithModules(final String apiBaseUrl, final String baseUrl) {

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        objectMapper.registerModule(new Module() {

            @Override
            public String getModuleName() {
                return "JsonIdHandler";
            }

            @Override
            public Version version() {
                return PackageVersion.VERSION;
            }

            @Override
            public void setupModule(SetupContext context) {
                context.appendAnnotationIntrospector(
                        new JsonIdAnnotationIntrospector(apiBaseUrl, baseUrl));
            }

        });

        converter.setObjectMapper(objectMapper);

        return converter;
    }
}
