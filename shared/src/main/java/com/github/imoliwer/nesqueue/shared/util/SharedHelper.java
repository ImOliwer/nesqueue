package com.github.imoliwer.nesqueue.shared.util;

import java.util.function.Consumer;

/**
 * This class represents the helper functionality all over 'shared'.
 */
public final class SharedHelper {
    /** No instantiation. **/
    private SharedHelper() throws IllegalAccessException {
        throw new IllegalAccessException("SharedHelper may not be instantiated.");
    }

    /**
     * Perform modifications on an object and then get the same instance returned.
     *
     * @param instance {@link Type} the instance to perform changes on.
     * @param handle {@link Consumer<Type>} changes to be applied to the passed down type.
     * @param <Type> type of object to perform modifications on.
     * @return {@link Type}
     */
    public static <Type> Type doWith(Type instance, Consumer<Type> handle) {
        if (instance == null)
            return null;
        handle.accept(instance);
        return instance;
    }
}