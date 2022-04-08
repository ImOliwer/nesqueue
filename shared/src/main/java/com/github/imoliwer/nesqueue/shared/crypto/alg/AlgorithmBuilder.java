package com.github.imoliwer.nesqueue.shared.crypto.alg;

/**
 * This interface represents the way of building an algorithm's initial.
 */
public interface AlgorithmBuilder {
    /**
     * Create a new anonymous object to build an algorithm initial from.
     *
     * @return {@link AlgorithmBuilder}
     */
    static AlgorithmBuilder begin() {
        return new AlgorithmBuilder() {
            private Alias algorithm;
            private Alias mode;
            private Alias padding;

            @Override
            public AlgorithmBuilder algorithm(Alias alias) {
                this.algorithm = alias;
                return this;
            }

            @Override
            public AlgorithmBuilder mode(Alias alias) {
                this.mode = alias;
                return this;
            }

            @Override
            public AlgorithmBuilder padding(Alias alias) {
                this.padding = alias;
                return this;
            }

            @Override
            public String build() {
                final StringBuilder builder = new StringBuilder();

                if (algorithm == null) {
                    throw new IllegalStateException("Algorithm must be present.");
                }

                builder.append(algorithm.alias());

                if (mode != null) {
                    builder.append('/');
                    builder.append(mode.alias());
                }

                if (padding != null) {
                    builder.append('/');
                    builder.append(padding.alias());
                }

                return builder.toString().trim();
            }
        };
    }

    /**
     * Set the algorithm for this builder.
     *
     * @param alias {@link Alias} the alias of said algorithm to use.
     * @return {@link AlgorithmBuilder}
     */
    AlgorithmBuilder algorithm(Alias alias);

    /**
     * Set the mode for this builder.
     *
     * @param alias {@link Alias} the alias of said mode to use.
     * @return {@link AlgorithmBuilder}
     */
    AlgorithmBuilder mode(Alias alias);

    /**
     * Set the padding for this builder.
     *
     * @param alias {@link Alias} the alias of said padding to use.
     * @return {@link AlgorithmBuilder}
     */
    AlgorithmBuilder padding(Alias alias);

    /**
     * Build the algorithm initial accordingly.
     *
     * @return {@link String} the algorithm that was built.
     */
    String build();
}