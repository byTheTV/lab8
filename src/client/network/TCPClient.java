package client.network;

import java.net.*;
import java.io.*;
import java.net.InetAddress;

public class TCPClient {
    private InetSocketAddress addr;
    private Socket socket;

    public TCPClient(InetAddress addr, int port) throws IOException {
        this.addr = new InetSocketAddress(addr, port);
        socket = new Socket();
        socket.connect(this.addr);
    }

    public void close() throws IOException {
        socket.close();
    }
}
