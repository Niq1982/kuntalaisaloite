package fi.om.municipalityinitiative.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static <T, E extends T> void copyFieldValuesToChild(T parent, E child) {

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
