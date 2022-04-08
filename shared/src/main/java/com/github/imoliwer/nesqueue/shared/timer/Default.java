package com.github.imoliwer.nesqueue.shared.timer;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

/**
 * This interface represents constants only relative to default {@link Timer} values.
 */
interface Default {
    /** {@link ScheduledExecutorService} the service used to perform default timing operations. **/
    ScheduledExecutorService SERVICE = newSingleThreadScheduledExecutor();
}