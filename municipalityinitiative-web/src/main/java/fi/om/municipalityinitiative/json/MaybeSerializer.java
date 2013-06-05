package fi.om.municipalityinitiative.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fi.om.municipalityinitiative.util.Maybe;

import java.io.IOException;

public class MaybeSerializer extends JsonSerializer<Maybe> {

    @Override
    public void serialize(Maybe value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value.isPresent()) {
            jgen.writeString(value.get().toString());
        }
        else {
            jgen.writeString("absent");
        }
    }

}
