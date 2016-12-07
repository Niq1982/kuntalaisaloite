package fi.om.municipalityinitiative.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.io.IOException;

public class JsonIdAnnotationIntrospector extends JacksonAnnotationIntrospector {
    
    private final String apiBaseUrl;
    private final String baseUrl;

    public JsonIdAnnotationIntrospector(String apiBaseUrl, String baseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.baseUrl = baseUrl;
    }


    @Override
    public Object findSerializer(Annotated a) {
        final JsonId ann = a.getAnnotation(JsonId.class);
        if (ann != null) {
            return new JsonSerializer<Long>() {

                @Override
                public void serialize(Long value, JsonGenerator jgen,
                        SerializerProvider provider) throws IOException {
                    jgen.writeString((ann.useApiUrl() ? apiBaseUrl : baseUrl) + ann.path().replace("{id}", value.toString()));
                }
            };
        } else {
            return super.findSerializer(a);
        }
    }

}
