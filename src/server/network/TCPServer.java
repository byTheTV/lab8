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
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Common.requests.Request;
import Common.responses.Response;

public class TCPServer {
    private final int port;
    private final RequestHandler requestHandler;
    private final ExecutorService executorService;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private boolean isRunning;

    public TCPServer(int port, RequestHandler requestHandler) {
        this.port = port;
        this.requestHandler = requestHandler;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        isRunning = true;
        System.out.println("Server started on port " + port);

        while (isRunning) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        executorService.submit(() -> {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                clientChannel.read(buffer);
                buffer.flip();

                ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Request request = (Request) ois.readObject();

                Response response = requestHandler.handleRequest(request);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(response);
                oos.flush();

                buffer.clear();
                buffer.put(baos.toByteArray());
                buffer.flip();

                while (buffer.hasRemaining()) {
                    clientChannel.write(buffer);
                }

                clientChannel.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error handling client request: " + e.getMessage());
                try {
                    clientChannel.close();
                } catch (IOException ex) {
                    System.err.println("Error closing client channel: " + ex.getMessage());
                }
            }
        });
    }

    public void stop() {
        isRunning = false;
        executorService.shutdown();
        try {
            if (selector != null) {
                selector.close();
            }
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
} 