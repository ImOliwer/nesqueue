package com.github.imoliwer.nesqueue.shared.timer;

import java.util.concurrent.TimeUnit;

import static com.github.imoliwer.nesqueue.shared.timer.Default.SERVICE;

final class DelayedImpl extends AbstractDefaultTimer {
    DelayedImpl(Callback<Timer> callback) {
        super(callback);
    }

    @Override
    public void start(TimeUnit unit, long value, boolean callInstantly) {
        this.stop();
        this.task = SERVICE.scheduleWithFixedDelay(
                () -> {
                    callback.invoke(this);
                    stop();
                },
                callInstantly ? 0 : value,
                value, unit
        );
    }

    @Override
    public byte type() {
        return DELAYED;
    }
}