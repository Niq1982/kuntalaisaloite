package fi.om.municipalityinitiative.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Optional;

public class OptionalSerializer extends JsonSerializer<Optional> {

    @Override
    public void serialize(Optional value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value.isPresent()) {
            jgen.writeString(value.get().toString());
        }
        else {
            jgen.writeString("empty");
        }
    }

}
