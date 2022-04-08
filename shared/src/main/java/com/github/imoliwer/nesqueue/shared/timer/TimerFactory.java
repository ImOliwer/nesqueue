package com.github.imoliwer.nesqueue.shared.timer;

import static com.github.imoliwer.nesqueue.shared.timer.Timer.*;

/**
 * This interface represents the factory for timers.
 */
@FunctionalInterface
public interface TimerFactory {
    /**
     * Create the default factory instance.
     *
     * @return {@link TimerFactory} the default instance.
     */
    static TimerFactory zero() {
        return (typeIdentifier, callback) -> {
            switch (typeIdentifier) {
                case REPEATING:
                    return new RepeatingImpl(callback);
                case DELAYED:
                    return new DelayedImpl(callback);
                default:
                    return null;
            }
        };
    }

    /**
     * Create a new timer by its type identifier with passed down callback.
     *
     * @param typeIdentifier {@link Byte} the timer's type identifier.
     * @param callback {@link Callback<Timer>} the operation to be performed.
     * @return {@link Timer} new instance of said timer, if identifier is valid, otherwise null.
     */
    Timer create(byte typeIdentifier, Callback<Timer> callback);
}