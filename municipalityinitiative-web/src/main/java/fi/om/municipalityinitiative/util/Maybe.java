package fi.om.municipalityinitiative.util;

import com.google.common.base.Optional;

/**
 * This class is a wrapper for com.google.common.base.Optional.
 * Made for freemarker so not only isPresent() can be called but also instead of get()
 * which freemarker is not able to call, it can use getValue().

 */
public class Maybe<T> {

    Optional<T> optional;

    public Maybe(Optional<T> optional) {
        this.optional = optional;
    }

    public static <T> Maybe<T> of(T object) {
        return new Maybe<T>(Optional.<T>of(object));
    }

    public static <T> Maybe<T> fromNullable(T object) {
        return new Maybe<T>(Optional.fromNullable(object));
    }

    public T get() {
        return optional.get();
    }

    public T getValue() {
        return get();
    }

    public boolean isPresent() {
        return optional.isPresent();
    }

    public static <T> Maybe<T> absent() {
        return new Maybe<T>(Optional.<T>absent());
    }

    public T orNull() {
        return optional.orNull();
    }
}
