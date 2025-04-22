package Server.network;

import Common.requests.Request;
import Common.responses.Response;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.Executors;

public class TCPServer {
    private final int port;
    private final RequestHandler handler;
    private ServerSocketChannel server;
    private Selector selector;

    public TCPServer(int port, RequestHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void start() throws IOException {
        server = ServerSocketChannel.open().bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);

        Executors.newSingleThreadExecutor().execute(() -> {
            while (server.isOpen()) {
                try {
                    selector.select();
                    for (var key : selector.selectedKeys()) {
                        if (key.isAcceptable()) {
                            accept(key);
                        } else if (key.isReadable()) {
                            read(key);
                        }
                    }
                    selector.selectedKeys().clear();
                } catch (IOException e) {
                    System.err.println("Server error: " + e.getMessage());
                }
            }
        });
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            // Чтение размера и данных
            ByteBuffer sizeBuf = ByteBuffer.allocate(4);
            if (client.read(sizeBuf) == -1) throw new IOException("Client disconnected");
            sizeBuf.flip();
            int size = sizeBuf.getInt();

            ByteBuffer dataBuf = ByteBuffer.allocate(size);
            if (client.read(dataBuf) == -1) throw new IOException("Client disconnected");
            dataBuf.flip();

            // Десериализация запроса
            byte[] data = new byte[size];
            dataBuf.get(data);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Request request = (Request) ois.readObject();
                Response response = handler.handleRequest(request);
                sendResponse(client, response);
            }
        } catch (IOException | ClassNotFoundException e) {
            closeClient(client);
        }
    }

    private void sendResponse(SocketChannel client, Response response) throws IOException {
        // Сериализация ответа
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        byte[] data = baos.toByteArray();

        // Отправка размера и данных
        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length).put(data);
        buffer.flip();
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
    }

    private void closeClient(SocketChannel client) {
        try {
            client.close();
        } catch (IOException ignored) {
        }
    }

    public void stop() {
        try {
            if (server != null) server.close();
        } catch (IOException ignored) {
        }
    }
}