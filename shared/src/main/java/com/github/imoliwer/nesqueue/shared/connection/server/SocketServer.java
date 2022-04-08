package com.github.imoliwer.nesqueue.shared.connection.server;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface SocketServer {
    boolean start();

    void stop();

    static SocketServer create(InetSocketAddress address, Options options) throws IOException {
        return new SocketServerImpl(address, options);
    }
}