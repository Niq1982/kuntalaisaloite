package fi.om.municipalityinitiative.util;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class MaybeHoldingHashMap<T, E>{

    Map<T, E> hashMap;

    public MaybeHoldingHashMap() {
        hashMap = Maps.newHashMap();
    }
    public MaybeHoldingHashMap(Map<T, E> originalMap) {
        hashMap = originalMap;
    }

    public int size() {
        return hashMap.size();
    }

    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    public Maybe<E> get(T key) {
        E maybeKey = hashMap.get(key);
        return Maybe.fromNullable(maybeKey);
    }

    public E put(T key, E value) {
        if (value == null)
            return value;
        hashMap.put(key, value);
        return value;
    }

    public void putAll(Map<? extends T, ? extends E> m) {
        hashMap.putAll(m);
    }

    public void clear() {
        hashMap.clear();
    }

    public Collection<E> values() {
        return hashMap.values();
    }
}
