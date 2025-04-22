package Server;

import Server.network.TCPServer;
import Server.network.ServerRequestHandler;
import Server.collectionManagers.*;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        StudyGroupCollectionManager collectionManager = new StudyGroupCollectionManager();
        ServerRequestHandler requestHandler = new ServerRequestHandler(collectionManager);
        TCPServer server = new TCPServer(5555, requestHandler); // Порт 5555
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }

}