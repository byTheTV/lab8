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
import Common.requests.AuthRequest;
import Common.responses.Response;
import Common.responses.AuthResponse;


public class TCPClient {
    private InetSocketAddress addr;
    private SocketChannel socketChannel;
    private static final int MAX_TIMEOUT = 30000; // Increased timeout to 30 seconds

    private static final int MAX_ATTEMPTS = 3;
    private static final int READ_TIMEOUT = 10000; // 10 seconds for read operations

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
                    TimeUnit.MILLISECONDS.sleep(10000);
                }
                TimeUnit.SECONDS.sleep(5);
                return;
            } catch (IOException | InterruptedException e) {
                attempts++;
                if (attempts == MAX_ATTEMPTS) {
                    throw new IOException("Failed to connect after " + MAX_ATTEMPTS + " attempts");
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Connection interrupted");
                }
            }
        }
    }

    public void sendRequest(Request request) throws IOException {
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }

        // Сериализация объекта
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
        }
        byte[] requestBytes = baos.toByteArray();

        // Отправка размера и данных
        ByteBuffer buffer = ByteBuffer.allocate(4 + requestBytes.length);
        buffer.putInt(requestBytes.length).put(requestBytes);
        buffer.flip();

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public void sendAuthRequest(String login, String password) throws IOException {
        AuthRequest request = new AuthRequest(login, password);
        sendRequest(request);
    }

    public AuthResponse receiveAuthResponse() throws IOException, ClassNotFoundException {
        return (AuthResponse) receiveResponse();
    }

    public Response receiveResponse() throws IOException, ClassNotFoundException {
        if (!isConnected()) {
            throw new IOException("Not connected to server");
        }

        // Чтение размера ответа
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        readFully(sizeBuffer, READ_TIMEOUT);
        sizeBuffer.flip();
        int responseSize = sizeBuffer.getInt();

        // Чтение ответа
        ByteBuffer responseBuffer = ByteBuffer.allocate(responseSize);
        readFully(responseBuffer, READ_TIMEOUT);
        responseBuffer.flip();

        // Десериализация ответа
        byte[] responseBytes = new byte[responseSize];
        responseBuffer.get(responseBytes);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
            return (Response) ois.readObject();
        }
    }

    // Вспомогательный метод для полного чтения данных с таймаутом
    private void readFully(ByteBuffer buffer, int timeoutMs) throws IOException {
        long startTime = System.currentTimeMillis();
        while (buffer.hasRemaining()) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                throw new IOException("Timeout while reading data");
            }
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Server disconnected");
            }
            if (bytesRead == 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Thread interrupted");
                }
            }
        }
    }

    public boolean isConnected() {
        return socketChannel != null && socketChannel.isOpen() && socketChannel.isConnected();
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
