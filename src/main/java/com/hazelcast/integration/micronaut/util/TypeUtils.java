package com.hazelcast.integration.micronaut.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Utility class for type operations.
 *
 */
public class TypeUtils {

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS =
            Collections.unmodifiableMap(new LinkedHashMap<Class<?>, Class<?>>() {
                {
                    put(boolean.class, Boolean.class);
                    put(byte.class, Byte.class);
                    put(char.class, Character.class);
                    put(double.class, Double.class);
                    put(float.class, Float.class);
                    put(int.class, Integer.class);
                    put(long.class, Long.class);
                    put(short.class, Short.class);
                    put(void.class, Void.class);
                }
            });

    /**
     * Finds the corresponding non-primitive {@code Class} of the primitive type.
     *
     * @param type the primitive type to look for
     *
     * @return the corresponding non-primitive {@link Class}
     */
    public static <T> Class<T> wrap(Class<T> type) {
        if (type.isPrimitive()) {
            return (Class<T>) PRIMITIVES_TO_WRAPPERS.get(type);
        } else {
            return type;
        }
    }
}
