package com.github.imoliwer.nesqueue.server.connection;

import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.Timer;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.github.imoliwer.nesqueue.server.connection.Options.Ignorance.BLACKLIST;
import static com.github.imoliwer.nesqueue.server.connection.Options.Ignorance.WHITELIST;
import static com.github.imoliwer.nesqueue.shared.timer.Timer.REPEATING;
import static com.github.imoliwer.nesqueue.shared.util.SharedHelper.doWith;
import static com.github.imoliwer.nesqueue.shared.timer.Timer.Callback;
import static com.github.imoliwer.nesqueue.shared.util.SharedHelper.serialize;
import static java.nio.ByteBuffer.allocate;
import static java.nio.ByteBuffer.wrap;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.StandardCharsets.UTF_8;

final class SocketServerImpl implements SocketServer {
    // handle related
    private final Thread thread;
    private final Timer sessionMonitorTimer;
    private final CryptoHandle cryptoHandle;
    private final Options options;

    // server & session related
    private final Selector selector;
    private final ServerSocketChannel channel;
    private final Set<SelectionKey> sessions;

    // states
    private boolean hasClosed;

    SocketServerImpl(InetSocketAddress address, TimerFactory timerFactory, CryptoHandle cryptoHandle, Options options) throws IOException {
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
        this.sessionMonitorTimer = timerFactory.create(REPEATING, getSessionMonitorCallback());
        this.cryptoHandle = cryptoHandle;
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
        if (hasClosed || thread.isAlive()) {
            return false;
        }
        thread.start();
        sessionMonitorTimer.start(TimeUnit.SECONDS, 5, false);
        return true;
    }

    @Override
    public void stop() {
        if (hasClosed)
            return;

        // interrupt the thread if it's alive
        if (thread.isAlive()) {
            thread.interrupt();
        }

        // stop the timer
        sessionMonitorTimer.stop();

        // handle termination
        try {
            final var sessionIterator = sessions.iterator();
            while (sessionIterator.hasNext()) {
                final var session = sessionIterator.next();
                session.channel().close();
                session.cancel();
                sessionIterator.remove();
            }

            channel.close();
            selector.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // modify state
        hasClosed = true;
    }

    @Override
    public <Type extends Serializable> void forward(SocketChannel channel, Type object) {
        if (hasClosed)
            return;

        final var encryptedBytes = cryptoHandle
            .encrypt(serialize(object))
            .getBytes(UTF_8);

        this.safeForward(channel, wrap(encryptedBytes));
    }

    @Override
    public <Type extends Serializable> void broadcast(Type object) {
        if (hasClosed)
            return;

        final var encryptedBytes = cryptoHandle.encrypt(serialize(object));
        final var buffer = wrap(encryptedBytes.getBytes(UTF_8));

        for (final SelectionKey key : this.sessions) {
            this.safeForward((SocketChannel) key.channel(), buffer);
        }
    }

    private void safeForward(SocketChannel channel, ByteBuffer buffer) {
        try {
            channel.write(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
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
                    sessions.add(key);
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

    private Callback<Timer> getSessionMonitorCallback() {
        return $ -> {
            final var iterator = sessions.iterator();
            var affected = 0;

            try {
                while (iterator.hasNext()) {
                    final var key = iterator.next();
                    final var channel = (SocketChannel) key.channel();

                    if (channel.isConnected()) {
                        continue;
                    }

                    channel.close();
                    key.cancel();
                    iterator.remove();
                    affected++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (affected <= 0) {
                return;
            }

            System.out.printf("Session Monitor >> Cleaned up %s disconnected sessions.\n", affected);
        };
    }
}