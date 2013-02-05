package fi.om.municipalityinitiative.util;

import fi.om.municipalityinitiative.newdto.ui.ContactInfo;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.joda.time.LocalDateTime;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;

public class ReflectionTestUtils {

    public static <T> T modifyAllFields(T bean) throws IllegalAccessException {
            for (Field field : bean.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers()))
                    field.set(bean, randomValue(field.getType()));
            }

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
        if (type == Maybe.class) {
            return Maybe.absent(); // TODO: find out the type of the optional object and recursively generate a random non-absent value
        }

        if (type == ContactInfo.class) {
            return modifyAllFields(new ContactInfo());
        }


        throw new IllegalArgumentException("unsupported type: " + type);
    }

    public static void assertNoNullFields(Object o) {
        for (Field field : o.getClass().getDeclaredFields()) {
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(o);
            } catch (IllegalAccessException e) {
                Assert.fail("Unable to get field value: " + field.getName());
            }
            assertThat("Field supposed to have value: "+ field.getName(), fieldValue, Matchers.is(Matchers.notNullValue()));
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
}
