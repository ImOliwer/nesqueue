package com.github.imoliwer.nesqueue.shared.util;

import java.io.*;
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

    /**
     * Serialize a serializable object by passed instance into a byte array.
     *
     * @param object {@link Type} the object to serialize.
     * @param <Type> the type to be serialized.
     * @return {@link Byte} array of bytes from serialized content.
     */
    public static <Type extends Serializable> byte[] serialize(Type object) {
        try {
            final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);

            // write and flush
            objectOutput.writeObject(object);
            objectOutput.flush();

            return byteOutput.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to serialize object.", ex);
        }
    }

    /**
     * Deserialize a byte array.
     *
     * @param bytes {@link Byte} the bytes to deserialize.
     * @param <Type> the type to be cast to upon deserialization.
     * @return {@link Type} the deserialized object.
     */
    public static <Type extends Serializable> Type deserialize(byte[] bytes) {
        try {
            final ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            final ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            // read
            return (Type) objectInputStream.readObject();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to deserialize and/or read object.", ex);
        }
    }
}