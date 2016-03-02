package fi.om.municipalityinitiative.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class DateTimeJsonSerializer extends JsonSerializer<DateTime> {

    private final DateTimeFormatter dtf = ISODateTimeFormat.dateTimeNoMillis();
    
    @Override
    public void serialize(DateTime value, JsonGenerator jgen,
            SerializerProvider provider) throws IOException {
        jgen.writeString(dtf.print(value));
    }

}
