package com.github.imoliwer.nesqueue.server;

import com.github.imoliwer.nesqueue.server.exception.ConverterReassignException;
import com.github.imoliwer.nesqueue.shared.Converter;

final class ServerImpl<Key, Relation, Data> implements NesQueueServer<Key, Relation, Data> {
    private Converter<Key, String> keyAdapter;
    private Converter<Relation, String> relationAdapter;

    @Override
    public void enter(Key queue, Relation key, Comparable<Data> data) {

    }

    @Override
    public void leave(Key queue, Relation key) {

    }

    @Override
    public void next(Key queue) {

    }

    @Override
    public void start() {

    }

    @Override
    public void setKeyAdapter(Converter<Key, String> adapter) {
        if (keyAdapter != null)
            throw new ConverterReassignException("key");
        this.keyAdapter = adapter;
    }

    @Override
    public void setRelationAdapter(Converter<Relation, String> converter) {
        if (relationAdapter != null)
            throw new ConverterReassignException("relation");
        this.relationAdapter = converter;
    }
}