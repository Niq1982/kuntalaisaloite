package fi.om.municipalityinitiative.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class LocalDateJsonSerializer extends JsonSerializer<LocalDate> {

    private final DateTimeFormatter dtf = ISODateTimeFormat.date();
    
    @Override
    public void serialize(LocalDate value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException {
        jgen.writeString(dtf.print(value));
    }

}
