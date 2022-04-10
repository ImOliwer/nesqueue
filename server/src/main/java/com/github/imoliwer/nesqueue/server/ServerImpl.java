package com.github.imoliwer.nesqueue.server;

import com.github.imoliwer.nesqueue.server.connection.Options;
import com.github.imoliwer.nesqueue.server.connection.SocketServer;
import com.github.imoliwer.nesqueue.server.exception.ConverterReassignException;
import com.github.imoliwer.nesqueue.shared.Converter;
import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Stream.generate;

final class ServerImpl<Relation, Data extends Comparable<Data>> implements NesQueueServer<Relation, Data> {
    static final class RelativeData<Relation, Data extends Comparable<Data>> implements Comparable<Data> {
        final Relation relation;
        final Data data;

        RelativeData(Relation relation, Data data) {
            this.relation = relation;
            this.data = data;
        }

        @Override
        public int compareTo(Data o) {
            return data.compareTo(o);
        }
    }

    static final class Notification implements Serializable {
        final String type;
        final int queueIndex;
        final Object relation;
        final Object data;

        public Notification(String type, Object relation, int queueIndex, Object data) {
            this.type = type;
            this.relation = relation;
            this.queueIndex = queueIndex;
            this.data = data;
        }
    }

    // handling
    private final SocketServer server;
    private Converter<Relation, String> relationAdapter;

    // storage
    private final Map<Relation, Integer> relationQueueIndexes;
    private final List<PriorityQueue<RelativeData<Relation, Data>>> queues;

    public ServerImpl(
        InetSocketAddress address,
        TimerFactory timerFactory,
        CryptoHandle cryptoHandle,
        Options options,
        int capacity
    ) throws IOException {
        this.server = SocketServer.create(
            address,
            timerFactory,
            cryptoHandle,
            options
        );
        this.relationQueueIndexes = new HashMap<>();
        this.queues = generate(() -> new PriorityQueue<RelativeData<Relation, Data>>())
            .limit(capacity)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public byte add(int queueIndex, Relation key, Data data) {
        if (!this.checkLength(queueIndex)) {
            server.broadcast(new Notification("join-invalid-queue", key, queueIndex, data));
            return INVALID_QUEUE;
        }

        final var oldQueueIndex = relationQueueIndexes.put(key, queueIndex);
        if (oldQueueIndex != null) {
            if (oldQueueIndex == queueIndex) {
                server.broadcast(new Notification("join-already-in-same-queue", key, queueIndex, data));
                return ALREADY_IN_SAME_QUEUE;
            }

            final var oldQueue = queues.get(oldQueueIndex);
            oldQueue.removeIf(it -> it.relation == key);
        }

        final var queue = queues.get(queueIndex);
        queue.add(new RelativeData<>(key, data));
        server.broadcast(new Notification("join-successful", key, queueIndex, data));
        return SUCCESSFUL;
    }

    @Override
    public byte remove(Relation key) {
        final var queueIndex = relationQueueIndexes.remove(key);
        if (queueIndex == null) {
            server.broadcast(new Notification("leave-not-in-queue", key, -1, null));
            return NOT_IN_QUEUE;
        }

        final var queue = queues.get(queueIndex);
        final var relativeDataAtomic = new AtomicReference<RelativeData<Relation, Data>>();

        queue.removeIf(it -> {
            final var isAuthor = it.relation == key;
            if (isAuthor) {
                relativeDataAtomic.set(it);
            }
            return isAuthor;
        });

        server.broadcast(new Notification("leave-successful", key, queueIndex, relativeDataAtomic.get()));
        return SUCCESSFUL;
    }

    @Override
    public void next(int queueIndex) {

    }

    @Override
    public boolean start() {
        return server.start();
    }

    @Override
    public void stop() {
        server.stop();
    }

    @Override
    public void setRelationAdapter(Converter<Relation, String> converter) {
        if (relationAdapter != null)
            throw new ConverterReassignException("relation");
        this.relationAdapter = converter;
    }

    private boolean checkLength(int index) {
        final var length = queues.size();
        return length > index && index >= 0;
    }
}