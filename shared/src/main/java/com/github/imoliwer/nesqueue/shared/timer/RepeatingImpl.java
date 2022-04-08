package com.github.imoliwer.nesqueue.shared.timer;

import java.util.concurrent.TimeUnit;

import static com.github.imoliwer.nesqueue.shared.timer.Default.SERVICE;

final class RepeatingImpl extends AbstractDefaultTimer {
    RepeatingImpl(Callback<Timer> callback) {
        super(callback);
    }

    @Override
    public void start(TimeUnit unit, long value, boolean callInstantly) {
        this.stop();
        this.task = SERVICE.scheduleAtFixedRate(
                () -> callback.invoke(this),
                callInstantly ? 0 : value,
                value, unit
        );
    }

    @Override
    public byte type() {
        return REPEATING;
    }
}