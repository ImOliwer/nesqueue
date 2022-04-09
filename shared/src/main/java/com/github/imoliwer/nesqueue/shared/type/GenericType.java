package com.github.imoliwer.nesqueue.shared.type;

import java.lang.reflect.ParameterizedType;

/**
 * This class is used to fetch the {@link Class} of
 * with the generic parameterized type.
 *
 * @param <T> the type to get.
 */
public final class GenericType<T> {
    /** {@link Class<T>} the class of said type. **/
    private final Class<T> clazz;

    /**
     * No public instantiation needed.
     */
    private GenericType() {
        this.clazz = (Class<T>) parameterized().getActualTypeArguments()[0];
    }

    /**
     * Get the parameterized type by superclass.
     *
     * @return {@link ParameterizedType}
     */
    private ParameterizedType parameterized() {
        return (ParameterizedType) getClass().getGenericSuperclass();
    }

    /**
     * Get the class of a parameterized type.
     *
     * @param <T> the type to fetch class' from.
     * @return {@link Class<T>}
     */
    public static <T> Class<T> of() {
        return new GenericType<T>().clazz;
    }
}