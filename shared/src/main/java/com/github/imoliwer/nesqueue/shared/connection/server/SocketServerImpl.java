package com.github.imoliwer.nesqueue.shared.connection.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

import static com.github.imoliwer.nesqueue.shared.connection.server.Options.Ignorance.BLACKLIST;
import static com.github.imoliwer.nesqueue.shared.connection.server.Options.Ignorance.WHITELIST;
import static com.github.imoliwer.nesqueue.shared.util.SharedHelper.doWith;
import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.StandardCharsets.UTF_8;

final class SocketServerImpl implements SocketServer {
    /**
     * {@link Thread} this field holds the thread used to handle client data.
     **/
    private final Thread thread;

    /**
     * {@link Selector} the selector used to handle client sessions.
     **/
    private final Selector selector;

    /**
     * {@link ServerSocketChannel} the channel used widely for communication.
     **/
    private final ServerSocketChannel channel;

    /**
     * {@link Set<SelectionKey>} hashset containing all active and authorized sessions.
     **/
    private final Set<SelectionKey> sessions;

    /**
     * {@link Options} the options instance passed through during instantiation.
     */
    private final Options options;

    SocketServerImpl(InetSocketAddress address, Options options) throws IOException {
        this.thread = new Thread(
            () -> {
                try {
                    final var reader = allocate(256);
                    while (true) {
                        handle(reader);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            },
            "Client Handle"
        );
        this.options = options.withAddress(address.getAddress().getHostAddress());
        this.selector = Selector.open();
        this.channel = doWith(ServerSocketChannel.open(), socketChannel -> {
            try {
                socketChannel.bind(address);
                socketChannel.configureBlocking(false);
                socketChannel.register(
                    this.selector,
                    OP_ACCEPT
                );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        this.sessions = new HashSet<>();
    }

    @Override
    public boolean start() {
        if (thread.isAlive()) {
            return false;
        }
        thread.start();
        return true;
    }

    @Override
    public void stop() {
        if (thread.isAlive()) {
            thread.interrupt();
        }
    }

    private void handle(ByteBuffer reader) throws IOException {
        selector.select();

        final var keys = selector.selectedKeys().iterator();
        final var ignoranceLevel = options.getIgnoranceLevel();

        while (keys.hasNext()) {
            final var key = keys.next();

            if (key.isAcceptable()) {
                final var clientChannel = (SocketChannel) channel
                    .accept()
                    .configureBlocking(false);
                final var clientAddress = clientChannel.getRemoteAddress().toString();
                final var hasClientAddress = options.hasAddress(clientAddress);

                System.out.printf("%s looking to be accepted\n", clientAddress); // TODO: remove after testing

                if (ignoranceLevel == BLACKLIST && hasClientAddress || ignoranceLevel == WHITELIST && !hasClientAddress) {
                    clientChannel.close();
                    key.cancel();
                } else {
                    clientChannel.register(this.selector, OP_READ);
                }
            }

            if (key.isReadable()) {
                // read from the client's channel
                final var clientChannel = (SocketChannel) key.channel();
                clientChannel.read(reader);

                // fetch necessary data to confirm event
                final var raw = UTF_8.decode(reader).toString();
                final var data = raw.split(";", 2);

                // handle accordingly
                switch (data[0]) {
                    // "addToQueue"?
                    // "removeFromQueue"?
                }
            }

            keys.remove();
        }
    }
}