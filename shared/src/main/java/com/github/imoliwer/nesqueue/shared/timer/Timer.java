package com.github.imoliwer.nesqueue.shared.timer;

import java.util.concurrent.TimeUnit;

/**
 * This interface represents the base of any timer.
 */
public interface Timer {
    /**
     * This interface represents a single function of which is used to handle
     * operations passed through timers.
     *
     * @param <T> {@link T} the type of timer.
     */
    @FunctionalInterface
    interface Callback<T extends Timer> {
        /**
         * Invoke the callback.
         *
         * @param timer {@link T} the timer in which was called.
         */
        void invoke(T timer);
    }

    /** {@link Byte} this represents the "id" of the 'repeating' timer. **/
    byte REPEATING = 0x00;

    /** {@link Byte} this represents the "id" of the 'delayed' timer. **/
    byte DELAYED   = 0x01;

    /**
     * Start this timer with corresponding options.
     *
     * @param unit {@link TimeUnit} the unit of which will be used to calculate the value in-between calls.
     * @param value {@link Long} the value to be used in-between calls.
     * @param callInstantly {@link Boolean} whether the first call will be instantly or within value.
     */
    void start(TimeUnit unit, long value, boolean callInstantly);

    /**
     * Stop the timer accordingly.
     */
    void stop();

    /**
     * Get the type of timer this is.
     *
     * @return {@link Byte} identifier of said timer's type.
     */
    byte type();

    /**
     * Get the current state of this timer.
     *
     * @return {@link Boolean} whether this timer is running.
     */
    boolean isRunning();
}