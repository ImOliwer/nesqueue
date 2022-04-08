package com.github.imoliwer.nesqueue.server;

import com.github.imoliwer.nesqueue.shared.Converter;

public interface NesQueueServer<Key, Relation, Data> {
    void enter(Key queue, Relation key, Comparable<Data> data);

    void leave(Key queue, Relation key);

    void next(Key queue);

    void start();

    void setKeyAdapter(Converter<Key, String> adapter);

    void setRelationAdapter(Converter<Relation, String> adapter);

    static <K, R, D> NesQueueServer<K, R, D> create() {
        return new ServerImpl<>();
    }
}