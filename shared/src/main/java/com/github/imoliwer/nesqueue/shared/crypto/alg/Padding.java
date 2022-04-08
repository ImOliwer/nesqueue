package com.github.imoliwer.nesqueue.shared.crypto.alg;

/**
 * This enumeration represents our default paddings for crypto algorithms.
 */
public enum Padding implements Alias {
    PKCS1("PKCS1Padding"),
    OAEP_WITH_SHA_512_AND_MGF1("OAEPWITHSHA-512ANDMGF1PADDING");

    private final String alias;

    Padding(String alias) {
        this.alias = alias;
    }

    Padding() {
        this(null);
    }

    @Override
    public String alias() {
        return alias == null ? name() : alias;
    }
}