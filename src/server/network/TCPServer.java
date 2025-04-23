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
import java.util.Iterator;

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
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
            client.read(sizeBuffer);
            sizeBuffer.flip();
            int size = sizeBuffer.getInt();

            ByteBuffer dataBuffer = ByteBuffer.allocate(size);
            client.read(dataBuffer);
            dataBuffer.flip();

            byte[] data = new byte[size];
            dataBuffer.get(data);

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        byte[] responseData = baos.toByteArray();

        ByteBuffer buffer = ByteBuffer.allocate(4 + responseData.length);
        buffer.putInt(responseData.length).put(responseData);
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
    }

    private void closeClient(SocketChannel client) {
        try {
            client.close();
        } catch (IOException ignored) {}
    }

    public void stop() throws IOException {
        server.close();
    }
}