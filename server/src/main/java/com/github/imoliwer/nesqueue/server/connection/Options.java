package com.github.imoliwer.nesqueue.server.connection;

import java.util.HashSet;
import java.util.Set;

import static com.github.imoliwer.nesqueue.server.connection.Options.Ignorance.ALLOW_ALL;
import static java.util.Collections.addAll;

/**
 * This class holds the necessary options for {@link SocketServer} implementation(s).
 * <p>
 * Reference: {@link SocketServerImpl}
 */
public final class Options {
    /**
     * This enumeration represents the ignorance levels of a {@link SocketServer}.
     */
    public enum Ignorance {
        WHITELIST,
        BLACKLIST,
        ALLOW_ALL
    }

    /**
     * {@link Ignorance} representative of what said server chose to have as ignorance level.
     **/
    private Ignorance ignoranceLevel;

    /**
     * {@link Set<String>} the addresses of which will be either whitelisted or blacklisted.
     **/
    private final Set<String> addresses;

    /**
     * Default level.
     */
    private Options() {
        this.ignoranceLevel = ALLOW_ALL;
        this.addresses = new HashSet<>();
    }

    /**
     * Apply this options instance with an ignorance level.
     *
     * @param level {@link Ignorance} the level of which to be set.
     * @return {@link Options} current instance.
     */
    public Options withIgnorance(Ignorance level) {
        this.ignoranceLevel = level == null ? ALLOW_ALL : level;
        return this;
    }

    /**
     * Add multiple or few addresses to the current state.
     *
     * @param addresses {@link String} array of addresses to be applied in this stance.
     * @return {@link Options} current instance.
     */
    public Options withAddress(String... addresses) {
        addAll(this.addresses, addresses);
        return this;
    }

    /**
     * Get whether an address has explicitly been added to this options instance.
     *
     * @param address {@link String} the address to check.
     * @return {@link Boolean} whether said address is existent.
     */
    public boolean hasAddress(String address) {
        return addresses.contains(address);
    }

    /**
     * Get the current ignorance level.
     *
     * @return {@link Ignorance}
     */
    public Ignorance getIgnoranceLevel() {
        return this.ignoranceLevel;
    }

    /**
     * Create a new, empty instance.
     *
     * @return {@link Options} fresh instance.
     */
    public static Options create() {
        return new Options();
    }
}