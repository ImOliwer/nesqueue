package com.github.imoliwer.nesqueue.shared.crypto.alg;

/**
 * This functional interface represents a single "value"
 * that of which is an alias in the form of a {@link String}.
 *
 * Merely used for functionalities such as {@link Algorithm},
 * {@link Mode} and {@link Padding} in relation to {@link AlgorithmBuilder}.
 */
@FunctionalInterface
public interface Alias {
    /**
     * Create a simple alias with it's {@link String} form attached.
     *
     * @param alias {@link String} the alias to set.
     * @return {@link Alias} new alias instance.
     */
    static Alias text(String alias) {
        return () -> alias;
    }

    /**
     * Get the alias of a parent function.
     *
     * @return {@link String}
     */
    String alias();
}