package com.github.imoliwer.nesqueue.server.connection;

import com.github.imoliwer.nesqueue.shared.base.NesQueueBase;
import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * This interface represents the server socket of said queue.
 */
public interface SocketServer extends NesQueueBase {
    /**
     * Serialize and forward an object to a channel.
     *
     * @param channel {@link SocketChannel} the channel to forward to.
     * @param object {@link Type} the object to be forwarded.
     */
    <Type extends Serializable> void forward(SocketChannel channel, Type object);

    /**
     * Serialize and broadcast an object to all the current available sessions.
     *
     * @param object {@link Type} the object to broadcast.
     */
    <Type extends Serializable> void broadcast(Type object);

    /**
     * Create a new instance of a socket server by address and options.
     *
     * @param address      {@link InetSocketAddress} the address to be bound to and started on.
     * @param timerFactory {@link TimerFactory} factory instance used to create the session monitoring timer.
     * @param cryptoHandle {@link CryptoHandle} the crypto handle instance to be used in encryption and decryption of forwarded messages.
     * @param options      {@link Options} the options to be bound to the new socket server.
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