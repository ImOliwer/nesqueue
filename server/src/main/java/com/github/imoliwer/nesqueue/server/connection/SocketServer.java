package com.github.imoliwer.nesqueue.server.connection;

import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
     * Forward bytes to a specific channel.
     *
     * @param channel {@link SocketChannel} the channel of which to forward the bytes to.
     * @param bytes {@link Byte} array of bytes to be forwarded.
     */
    void forward(SocketChannel channel, byte[] bytes);

    /**
     * Broadcast a text to all the current available sessions.
     *
     * @param message {@link String} the message to be forwarded.
     */
    void broadcast(String message);

    /**
     * Create a new instance of a socket server by address and options.
     *
     * @param address {@link InetSocketAddress} the address to be bound to and started on.
     * @param timerFactory {@link TimerFactory} factory instance used to create the session monitoring timer.
     * @param cryptoHandle {@link CryptoHandle} the crypto handle instance to be used in encryption and decryption of forwarded messages.
     * @param options {@link Options} the options to be bound to the new socket server.
     * @return {@link SocketServer}
     * @throws IOException if there was an error during creation of the socket server.
     */
    static SocketServer create(
            InetSocketAddress address,
            TimerFactory timerFactory,
            CryptoHandle cryptoHandle,
            Options options
    ) throws IOException {
        return new SocketServerImpl(address, timerFactory, cryptoHandle, options);
    }
}