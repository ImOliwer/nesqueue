package com.github.imoliwer.nesqueue.server;

import com.github.imoliwer.nesqueue.server.connection.Options;
import com.github.imoliwer.nesqueue.shared.Converter;
import com.github.imoliwer.nesqueue.shared.base.NesQueueBase;
import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This interface represents the NesQueue server.
 *
 * @param <Relation> type of queue-to-client relation to store in each queue.
 * @param <Data>     type of data to store and make use of in update notifications.
 */
public interface NesQueueServer<Relation, Data> extends NesQueueBase {
    /** {@link Byte} id of the 'successful' state. **/
    byte SUCCESSFUL            = 0x00;

    /** {@link Byte} id of the 'queue index is not existent' state. **/
    byte INVALID_QUEUE         = 0x01;

    /** {@link Byte} id of the 'relation is already in the same queue' state. **/
    byte ALREADY_IN_SAME_QUEUE = 0x02;

    /** {@link Byte} id of the 'relation is not in a queue' state. **/
    byte NOT_IN_QUEUE          = 0x03;

    /**
     * Add a new relation with data to queue by its key.
     *
     * @param queueIndex {@link Integer} the queue to add said relation to.
     * @param key        {@link Relation} the key to be added.
     * @param data       {@link Data} the data to be linked.
     * @return {@link Byte} result of this addition.
     */
    byte add(int queueIndex, Relation key, Data data);

    /**
     * Remove a relation from queue by key.
     *
     * @param key {@link Relation} the key of said relation to remove.
     * @return {@link Byte} result of this removal.
     */
    byte remove(Relation key);

    /**
     * Handle the next relation in queue by key.
     *
     * @param queueIndex {@link Integer} the queue to perform next op on.
     */
    void next(int queueIndex);

    /**
     * Set the converter for relational keys.
     *
     * @param adapter {@link Converter} the converter to be assigned.
     */
    void setRelationAdapter(Converter<Relation, String> adapter);

    /**
     * Create a new NesQueue server.
     *
     * @param <R> the relational key type.
     * @param <D> the data key type.
     * @return {@link NesQueueServer} new instance of a NesQueue server.
     */
    static <R, D extends Comparable<D>> NesQueueServer<R, D> create(
        InetSocketAddress address,
        TimerFactory timerFactory,
        CryptoHandle cryptoHandle,
        Options options,
        int queueCapacity
    ) throws IOException {
        return new ServerImpl<>(address, timerFactory, cryptoHandle, options, queueCapacity);
    }
}