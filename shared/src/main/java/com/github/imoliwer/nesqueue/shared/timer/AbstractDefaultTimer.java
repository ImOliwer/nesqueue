package com.github.imoliwer.nesqueue.shared.timer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

abstract class AbstractDefaultTimer implements Timer {
    protected final Callback<Timer> callback;
    protected ScheduledFuture<?> task;

    AbstractDefaultTimer(Callback<Timer> callback) {
        if (callback == null)
            throw new IllegalArgumentException("Callback must not be null");
        this.callback = callback;
    }

    @Override
    public abstract void start(TimeUnit unit, long value, boolean callInstantly);

    @Override
    public final void stop() {
        if (task == null) {
            return;
        }
        task.cancel(false);
        task = null;
    }

    @Override
    public boolean isRunning() {
        return task != null;
    }
}