package com.github.imoliwer.nesqueue.shared.connection.server;

import java.util.HashSet;
import java.util.Set;

import static com.github.imoliwer.nesqueue.shared.connection.server.Options.Ignorance.ALLOW_ALL;
import static java.util.Collections.addAll;

public final class Options {
    public enum Ignorance {
        WHITELIST,
        BLACKLIST,
        ALLOW_ALL
    }

    private Ignorance ignoranceLevel;
    private final Set<String> addresses;

    private Options() {
        this.ignoranceLevel = ALLOW_ALL;
        this.addresses = new HashSet<>();
    }

    public Options withIgnorance(Ignorance level) {
        this.ignoranceLevel = level == null ? ALLOW_ALL : level;
        return this;
    }

    public Options withAddress(String... addresses) {
        addAll(this.addresses, addresses);
        return this;
    }

    public boolean hasAddress(String address) {
        return addresses.contains(address);
    }

    public Ignorance getIgnoranceLevel() {
        return this.ignoranceLevel;
    }

    public static Options create() {
        return new Options();
    }
}