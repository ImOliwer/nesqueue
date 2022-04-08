package com.github.imoliwer.nesqueue.shared.crypto.alg;

import java.security.Key;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static java.security.KeyFactory.getInstance;

/**
 * This class represents the helper for private and public keys in relation
 * to {@link com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle}.
 */
public final class CryptoKey {
    /**
     * This enumeration holds the two types of keys there are - private and public.
     */
    public enum Type {
        PRIVATE,
        PUBLIC
    }

    /** No instantiation. **/
    private CryptoKey() throws IllegalAccessException {
        throw new IllegalAccessException("Key may not be instantiated.");
    }

    /**
     * Fetch an existing key by algorithm, type and said key input.
     *
     * @param algorithm {@link String} the algorithm to use during generation.
     * @param type {@link Type} the type of key to fetch.
     * @param input {@link Byte} array of bytes containing the raw key input.
     * @param <K> type of key to return by casting to the generated one. Ensure it's a valid type.
     * @return {@link K} the generated key.
     * @throws Exception if an error occurs during the generation.
     */
    public static <K extends Key> K of(String algorithm, Type type, byte[] input) throws Exception {
        final var factory = getInstance(algorithm);

        if (type == Type.PRIVATE) {
            return (K) factory.generatePrivate(new PKCS8EncodedKeySpec(input));
        }

        return (K) factory.generatePublic(new X509EncodedKeySpec(input));
    }

    /**
     * @see CryptoKey#of(String, Type, byte[])
     */
    public static <K extends Key> K ensuredOf(String algorithm, Type type, byte[] input) {
        try { return of(algorithm, type, input); }
        catch (Exception ignored) { return null; }
    }
}