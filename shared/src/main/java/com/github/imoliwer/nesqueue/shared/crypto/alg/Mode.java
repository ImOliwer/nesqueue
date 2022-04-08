package com.github.imoliwer.nesqueue.shared.crypto.alg;

/**
 * This enumeration represents our default modes for crypto algorithms.
 */
public enum Mode implements Alias {
    ECB;

    private final String alias;

    Mode(String alias) {
        this.alias = alias;
    }

    Mode() {
        this(null);
    }

    @Override
    public String alias() {
        return alias == null ? name() : alias;
    }
}