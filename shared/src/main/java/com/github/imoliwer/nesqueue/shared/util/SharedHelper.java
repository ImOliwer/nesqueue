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

    public static <Type> Type doWith(Type instance, Consumer<Type> handle) {
        if (instance == null)
            return null;
        handle.accept(instance);
        return instance;
    }
}