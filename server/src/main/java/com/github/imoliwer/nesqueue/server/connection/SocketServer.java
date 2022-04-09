package com.github.imoliwer.nesqueue.server.connection;

import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This interface represents the server socket of said queue.
 */
public interface SocketServer {
    /**
     * Start this socket server.
     *
     * @return {@link Boolean} whether it was started successfully or not.
     */
    boolean start();

    /**
     * Stop this socket server.
     */
    void stop();

    /**
     * Create a new instance of a socket server by address and options.
     *
     * @param address {@link InetSocketAddress} the address to be bound to and started on.
     * @param cryptoHandle {@link CryptoHandle} the crypto handle instance to be used in encryption and decryption of forwarded messages.
     * @param options {@link Options} the options to be bound to the new socket server.
     * @return {@link SocketServer}
     * @throws IOException if there was an error during creation of the socket server.
     */
    static SocketServer create(InetSocketAddress address, CryptoHandle cryptoHandle, Options options) throws IOException {
        return new SocketServerImpl(address, cryptoHandle, options);
    }
}