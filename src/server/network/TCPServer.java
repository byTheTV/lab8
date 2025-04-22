package Server.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

import Common.requests.Request;
import Common.responses.Response;

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
        try {
            server = ServerSocketChannel.open().bind(new InetSocketAddress(port));
            server.configureBlocking(false);
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Сервер успешно запущен на порту " + port);
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
            throw e;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            while (server.isOpen()) {
                try {
                    selector.select();
                    for (var key : selector.selectedKeys()) {
                        try {
                            if (key.isAcceptable()) {
                                accept(key);
                            } else if (key.isReadable()) {
                                read(key);
                            }
                        } catch (IOException e) {
                            System.err.println("Ошибка при обработке соединения: " + e.getMessage());
                            if (key.channel() instanceof SocketChannel) {
                                closeClient((SocketChannel) key.channel());
                            }
                        }
                    }
                    selector.selectedKeys().clear();
                } catch (IOException e) {
                    System.err.println("Ошибка в основном цикле сервера: " + e.getMessage());
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
            int bytesRead = client.read(sizeBuf);
            if (bytesRead == -1) {
                System.err.println("Клиент отключился при чтении размера данных");
                throw new IOException("Client disconnected");
            }
            sizeBuf.flip();
            int size = sizeBuf.getInt();
            System.out.println("Получен размер данных: " + size + " байт");

            ByteBuffer dataBuf = ByteBuffer.allocate(size);
            bytesRead = client.read(dataBuf);
            if (bytesRead == -1) {
                System.err.println("Клиент отключился при чтении данных");
                throw new IOException("Client disconnected");
            }
            dataBuf.flip();

            // Десериализация запроса
            byte[] data = new byte[size];
            dataBuf.get(data);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                Request request = (Request) ois.readObject();
                System.out.println("Получен запрос: " + request.getClass().getSimpleName());
                Response response = handler.handleRequest(request);
                sendResponse(client, response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при обработке запроса: " + e.getMessage());
            closeClient(client);
        }
    }

    private void sendResponse(SocketChannel client, Response response) throws IOException {
        try {
            // Сериализация ответа
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(response);
            }
            byte[] data = baos.toByteArray();
            System.out.println("Отправляется ответ размером: " + data.length + " байт");

            // Отправка размера и данных
            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length).put(data);
            buffer.flip();
            while (buffer.hasRemaining()) {
                int bytesWritten = client.write(buffer);
                if (bytesWritten == -1) {
                    System.err.println("Ошибка при отправке данных: клиент отключился");
                    throw new IOException("Client disconnected during write");
                }
            }
            System.out.println("Ответ успешно отправлен");
        } catch (IOException e) {
            System.err.println("Ошибка при отправке ответа: " + e.getMessage());
            throw e;
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