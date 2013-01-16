package fi.om.municipalityinitiative.util;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

public class TestUtils {

    public static void assertNoNullFields(Object o) {
        for (Field field : o.getClass().getDeclaredFields()) {
            Object fieldValue = null;
            try {
                field.setAccessible(true);
                fieldValue = field.get(o);
            } catch (IllegalAccessException e) {
                fail("Unable to get field value: " + field.getName());
            }
            assertThat("Field supposed to have value: "+ field.getName(), fieldValue, is(notNullValue()));
        }
    }

    /**public static void assertAllFields(Object o1, Object o2) {

        assertThat(o1.getClass(), is(o2.getClass()));

        for (Field field : o1.getClass().getDeclaredFields()) {
            Object o1Value = null;
            Object o2Value = null;
            try {
                field.setAccessible(true);
                o1Value = field.get(o1);
                o2Value = field.get(o2);
            } catch (IllegalAccessException e) {
                fail("Unable to get field value: " + field.getName());
            }
            assertThat("Field values match: "+ field.getName(), o1Value, is(o2Value));
        }

    }                       **/
}
