package Server;

import Server.network.TCPServer;
import Server.network.ServerRequestHandler;
import Server.collectionManagers.*;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try {
            int port = 1234; // Default port
            if (args.length > 0) {
                port = Integer.parseInt(args[0]);
            }

            StudyGroupCollectionManager collectionManager = new StudyGroupCollectionManager();
            ServerRequestHandler requestHandler = new ServerRequestHandler(collectionManager);
            TCPServer server = new TCPServer(port, requestHandler);

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                server.stop();
            }));

            server.start();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            System.exit(1);
        }
    }
} 