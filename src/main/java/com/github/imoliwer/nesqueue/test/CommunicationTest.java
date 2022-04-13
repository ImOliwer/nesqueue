package com.github.imoliwer.nesqueue.test;

import com.github.imoliwer.nesqueue.server.NesQueueServer;
import com.github.imoliwer.nesqueue.server.connection.Options;
import com.github.imoliwer.nesqueue.shared.crypto.CryptoHandle;
import com.github.imoliwer.nesqueue.shared.timer.TimerFactory;
import com.github.imoliwer.nesqueue.shared.util.SharedHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.github.imoliwer.nesqueue.server.connection.Options.Ignorance.ALLOW_ALL;

public class CommunicationTest {
    public static final class Customer implements Comparable<Customer>, Serializable {
        private final byte rank;

        public Customer(byte rank) {
            this.rank = rank;
        }

        @Override
        public int compareTo(Customer o) {
            if (rank == o.rank)
                return 0;
            return rank > o.rank ? 1 : -1;
        }

        @Override
        public String toString() {
            return "Customer{" +
                "rank=" + rank +
                '}';
        }
    }

    private static final CryptoHandle CRYPTO_HANDLE = new CryptoHandle("RSA", null, null);

    public static void main(String[] args) throws IOException {
        final NesQueueServer<UUID, Customer> server = NesQueueServer.create(
            new InetSocketAddress(4090),
            TimerFactory.zero(),
            CRYPTO_HANDLE,
            Options.create().withIgnorance(ALLOW_ALL),
            1
        );

        if (server.start()) {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(4090));

            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    int read;

                    while (true) {
                        read = is.read(buffer);
                        if (read == -1) {
                            continue;
                        }
                        final var decryptedAndDeserialized = SharedHelper.deserialize(
                            CRYPTO_HANDLE
                                .decrypt(new String(buffer, 0, read).getBytes(StandardCharsets.UTF_8))
                                .getBytes(StandardCharsets.UTF_8)
                        );
                        System.out.printf("Client >> %s", decryptedAndDeserialized);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            server.add(0, UUID.randomUUID(), new Customer((byte) 1));
        }

        while (true) {}
    }
}