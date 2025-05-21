package Server.network;

import Common.requests.Request;
import Common.responses.Response;

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
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TCPServer {
    private static class ClientHandler {
        final SocketChannel channel;
        final Lock readLock = new ReentrantLock();
        final Lock writeLock = new ReentrantLock();
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        ByteBuffer dataBuffer;
        boolean readingSize = true;

        // Буферы для отправки ответа
        ByteBuffer responseSizeBuffer;
        ByteBuffer responseDataBuffer;
        boolean writingSize = false;

        ClientHandler(SocketChannel channel) {
            this.channel = channel;
        }
    }

    private final int port;
    private final RequestHandler handler;
    private ServerSocketChannel server;
    private Selector selector;
    private final ExecutorService readThreadPool;
    private final ForkJoinPool processPool;
    private final ForkJoinPool sendPool;

    public TCPServer(int port, RequestHandler handler, int readThreads, int processThreads, int sendThreads) {
        this.port = port;
        this.handler = handler;
        this.readThreadPool = Executors.newFixedThreadPool(readThreads);
        this.processPool = new ForkJoinPool(processThreads);
        this.sendPool = new ForkJoinPool(sendThreads);
    }

    public void start() throws IOException {
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Сервер запущен на порту " + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        client.configureBlocking(false);
        SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
        clientKey.attach(new ClientHandler(client));
        System.out.println("Новый клиент: " + client.getRemoteAddress());
    }

    private void read(SelectionKey key) {
        ClientHandler handler = (ClientHandler) key.attachment();
        SocketChannel client = handler.channel;
        readThreadPool.submit(() -> {
            handler.readLock.lock();
            try {
                boolean dataAvailable = true;
                while (dataAvailable) {
                    if (handler.readingSize) {
                        int bytesRead = client.read(handler.sizeBuffer);
                        if (bytesRead == -1) {
                            throw new IOException("Client disconnected");
                        }
                        if (bytesRead == 0) {
                            dataAvailable = false; // Нет больше данных сейчас
                        } else if (handler.sizeBuffer.position() == 4) {
                            handler.sizeBuffer.flip();
                            int size = handler.sizeBuffer.getInt();
                            handler.dataBuffer = ByteBuffer.allocate(size);
                            handler.readingSize = false;
                            handler.sizeBuffer.clear();
                        }
                    } else {
                        int bytesRead = client.read(handler.dataBuffer);
                        if (bytesRead == -1) {
                            throw new IOException("Client disconnected");
                        }
                        if (bytesRead == 0) {
                            dataAvailable = false; // Нет больше данных сейчас
                        } else if (handler.dataBuffer.position() == handler.dataBuffer.capacity()) {
                            handler.dataBuffer.flip();
                            byte[] data = new byte[handler.dataBuffer.capacity()];
                            handler.dataBuffer.get(data);
                            processRequest(client, data);
                            handler.dataBuffer = null;
                            handler.readingSize = true;
                        }
                    }
                }
            } catch (IOException e) {
                closeClient(client);
            } finally {
                handler.readLock.unlock();
            }
        });
    }

    private void processRequest(SocketChannel client, byte[] data) {
        processPool.submit(() -> {
            try {
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    Request request = (Request) ois.readObject();
                    Response response = handler.handleRequest(request);
                    sendResponse(client, response);
                }
            } catch (Exception e) {
                System.err.println("Ошибка обработки запроса: " + e.getMessage());
                closeClient(client);
            }
        });
    }

    private void sendResponse(SocketChannel client, Response response) {
        sendPool.submit(() -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(response);
                }
                byte[] responseData = baos.toByteArray();
                ByteBuffer sizeBuffer = ByteBuffer.allocate(4).putInt(responseData.length).flip();
                ByteBuffer dataBuffer = ByteBuffer.wrap(responseData);

                SelectionKey key = client.keyFor(selector);
                if (key == null) return;
                ClientHandler handler = (ClientHandler) key.attachment();

                handler.writeLock.lock();
                try {
                    handler.responseSizeBuffer = sizeBuffer;
                    handler.responseDataBuffer = dataBuffer;
                    handler.writingSize = true;
                    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    selector.wakeup();
                } finally {
                    handler.writeLock.unlock();
                }
            } catch (IOException e) {
                System.err.println("[Сервер] Ошибка подготовки ответа: " + e.getMessage());
                closeClient(client);
            }
        });
    }

    private void write(SelectionKey key) {
        ClientHandler handler = (ClientHandler) key.attachment();
        SocketChannel client = handler.channel;

        if (!client.isConnected()) {
            closeClient(client);
            return;
        }

        handler.writeLock.lock();
        try {
            if (handler.writingSize) {
                client.write(handler.responseSizeBuffer);
                if (handler.responseSizeBuffer.hasRemaining()) {
                    key.interestOps(SelectionKey.OP_WRITE);
                    return;
                }
                handler.writingSize = false;
            }

            if (!handler.writingSize) {
                client.write(handler.responseDataBuffer);
                if (handler.responseDataBuffer.hasRemaining()) {
                    key.interestOps(SelectionKey.OP_WRITE);
                } else {
                    // Снимаем флаг записи и возвращаем интерес на чтение
                    key.interestOps(SelectionKey.OP_READ);
                    handler.responseSizeBuffer = null;
                    handler.responseDataBuffer = null;
                }
            }
        } catch (IOException e) {
            closeClient(client);
        } finally {
            handler.writeLock.unlock();
        }
    }

    private void closeClient(SocketChannel client) {
        System.out.println("[Сервер] Клиент отключен: " + client.socket().getRemoteSocketAddress());
        try {
            if (client.isOpen()) client.close();
        } catch (IOException ignored) {}
    }

    public void stop() throws IOException {
        server.close();
        readThreadPool.shutdown();
        processPool.shutdown();
        sendPool.shutdown();
    }
}