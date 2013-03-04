package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.json.ObjectSerializer;
import fi.om.municipalityinitiative.newdto.service.Municipality;
import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import fi.om.municipalityinitiative.newdto.ui.MunicipalityInfo;
import fi.om.municipalityinitiative.newdto.ui.Participants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

/**
 * Sets random values to all fields and parents fields.

 */
public class ReflectionTestUtils {

    public static <T> T modifyAllFields(T bean)  {

        Class clazz = bean.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    try {
                        field.set(bean, randomValue(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
            }
            clazz = clazz.getSuperclass();
        }
        while (clazz != null && clazz != Object.class);

        try {
            assertNoNullFields(bean);
        } catch (AssertionError e) {
            fail("Unable to randomize all field values, something was null: " + e.getMessage());
        }
        return bean;
    }

    public static <T> void assertReflectionEquals(T o1, T o2) {
        assertThat("Reflected fields should match", ReflectionToStringBuilder.reflectionToString(o1, ToStringStyle.SHORT_PREFIX_STYLE),
                is(ReflectionToStringBuilder.reflectionToString(o2, ToStringStyle.SHORT_PREFIX_STYLE)));
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
            int i = randomInt() % constants.length;
            if (i < 0) i*= -1;
            return constants[i];
        }
        if (type == LocalDateTime.class) {
            return new LocalDateTime(randomLong());
        }
        if (type == LocalDate.class){
            return new LocalDate(randomLong());
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

        if (type == Participants.class) {
            return modifyAllFields(new Participants());
        }
        if (type == Municipality.class) {
            return modifyAllFields(new Municipality(0, "", ""));
        }
        if (type.equals(MunicipalityInfo.class)) {
            return modifyAllFields(new MunicipalityInfo());
        }
        if (type.equals(List.class)) {
            return new ArrayList<>();
        }

        throw new IllegalArgumentException("unsupported type: " + type);
    }

    /**
     * Asserts if any fields are null.
     * @param o
     */
    public static void assertNoNullFields(Object o){
            assertThat(ObjectSerializer.objectToString(o), not(containsString(":null")));
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

}
