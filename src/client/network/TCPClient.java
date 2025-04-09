package client.network;

import java.net.*;
import java.io.*;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class TCPClient {
    private InetSocketAddress addr;
    private SocketChannel socketChannel;
    private static final int MAX_TIMEOUT = 5000; //MS
    private static final int MAX_ATTEMPTS = 3; // attempts to connect

    public TCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        initializeConnection();
    }

    private void initializeConnection() throws IOException{
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS){
            try {
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect(addr);

                long startTime = System.currentTimeMillis();
                while (!socketChannel.finishConnect()) {
                    if (System.currentTimeMillis() - startTime > MAX_TIMEOUT) {
                        throw new IOException("Connection timeout");
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                }
                return;
            } catch (IOException | InterruptedException e){
                attempts++;
                if(attempts == MAX_ATTEMPTS){
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

    public void close() throws IOException {
        //socket.close();
    }
}
