package fi.om.municipalityinitiative.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static <P, C extends P> void copyFieldValuesToChild(P parent, C child) {

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
