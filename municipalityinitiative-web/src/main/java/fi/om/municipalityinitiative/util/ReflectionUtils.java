package fi.om.municipalityinitiative.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void copyFieldValuesToChild(Object parent, Object child) {

        try {
            for (Field field : parent.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                field.set(child, field.get(parent));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
