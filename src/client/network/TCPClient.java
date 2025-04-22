package Client.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import Common.requests.Request;
import Common.responses.Response;

public class TCPClient {
    private InetSocketAddress addr;
    private SocketChannel socketChannel;
    private static final int MAX_TIMEOUT = 5000; // MS
    private static final int MAX_ATTEMPTS = 3; // attempts to connect

    public TCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        initializeConnection();
    }

    private void initializeConnection() throws IOException {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect(addr);

                long startTime = System.currentTimeMillis();
                while (!socketChannel.finishConnect()) {
                    if (System.currentTimeMillis() - startTime > MAX_TIMEOUT) {
                        throw new IOException("Connection timeout");
                    }
                    TimeUnit.MILLISECONDS.sleep(1000);
                }
                return;
            } catch (IOException | InterruptedException e) {
                attempts++;
                if (attempts == MAX_ATTEMPTS) {
                    throw new IOException("Failed to connect");
                }
                System.err.println("Connection failed, retrying...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // restore interrupted status
                    throw new IOException("Thread interrupted during retry", ie);
                }
            }
        }
    }

    /**
     * Проверяет, активно ли соединение с сервером.
     *
     * @return true, если канал открыт и соединение установлено, иначе false
     */
    public boolean isConnected() {
        return socketChannel != null && socketChannel.isOpen() && socketChannel.isConnected();
    }

    public void sendRequest(Request request) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();
        buffer.put(baos.toByteArray());
        buffer.flip();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer);
        buffer.flip();
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (Response) ois.readObject();
    }

    public void close() {
        try {
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing channel: " + e.getMessage());
        }
    }
}