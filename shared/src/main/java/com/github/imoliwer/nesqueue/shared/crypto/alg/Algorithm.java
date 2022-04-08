package com.github.imoliwer.nesqueue.shared.crypto.alg;

/**
 * This enumeration represents our default algorithms.
 */
public enum Algorithm implements Alias {
    RSA;

    private final String alias;

    Algorithm(String alias) {
        this.alias = alias;
    }

    Algorithm() {
        this(null);
    }

    @Override
    public String alias() {
        return alias == null ? name() : alias;
    }
}