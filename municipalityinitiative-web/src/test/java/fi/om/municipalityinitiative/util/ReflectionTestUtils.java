package fi.om.municipalityinitiative.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

public class ReflectionTestUtils {

    public static <T> T modifyAllFields(T bean) throws IllegalAccessException {
            for (Field field : bean.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    field.set(bean, randomValue(field.getType()));
            }

        assertNoNullFields(bean);
        return bean;
    }

    private static Object randomValue(Class<?> type) throws IllegalAccessException {

        // standard Java types
        if (type == String.class) {
            return randomString();
        }
        if (type == int.class || type == Integer.class) {
            return randomInt();
        }
        if (type == long.class || type == Long.class) {
            return randomLong();
        }
        if (type == boolean.class) {
            return true;
        }
        if (type == Boolean.class) {
            return true;
        }
        if (type.isEnum()) {
            Object[] constants = type.getEnumConstants();
            return constants[randomInt() % constants.length];
        }
        if (type == LocalDateTime.class) {
            return new LocalDateTime(randomLong());
        }
        if (type == DateTime.class) {
            return new DateTime(randomLong());
        }
        if (type == Maybe.class) {
            return Maybe.absent(); // TODO: find out the type of the optional object and recursively generate a random non-absent value
        }

        if (type == ContactInfo.class) {
            return modifyAllFields(new ContactInfo());
        }

        throw new IllegalArgumentException("unsupported type: " + type);
    }

    public static void assertNoNullFields(Object o){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibilityChecker(mapper.getVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY));

        mapper.registerModule(new MaybeModule());

        try {
            assertThat(mapper.writeValueAsString(o), not(containsString(":null")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int randomInt() {
        return new Random().nextInt();
    }

    private static long randomLong() {
        return new Random().nextLong();
    }

    private static String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static final class MaybeModule extends SimpleModule {
        public MaybeModule() {
            addSerializer(Maybe.class, new MaybeSerializer());
            addSerializer(DateTime.class, new DateTimeSerializer());
        }
    }

    private static class DateTimeSerializer extends StdSerializer<DateTime> {
        protected DateTimeSerializer() {
            super(DateTime.class);
        }

        @Override
        public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value == null ? "null" : value.toString());
        }
    }

    private static class MaybeSerializer extends StdSerializer<Maybe> {

        protected MaybeSerializer() {
            super(Maybe.class);
        }

        @Override
        public void serialize(Maybe value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value.isPresent()) {
                jgen.writeString(value.get().toString());
            }
            else {
                jgen.writeString("absent");
            }
        }

    }
}
