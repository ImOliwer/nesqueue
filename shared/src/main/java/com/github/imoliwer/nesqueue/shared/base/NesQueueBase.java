package com.github.imoliwer.nesqueue.shared.base;

/**
 * This interface represents the base of NesQueue servers & clients.
 */
public interface NesQueueBase {
    /**
     * Start the implementation of this base.
     *
     * @return {@link Boolean} whether the startup was successful or not.
     */
    boolean start();

    /**
     * Stop the implementation of this base.
     */
    void stop();
}