package Server;

import java.io.IOException;

import Common.models.StudyGroup;
import Common.models.StudyGroupExample;
import Server.collectionManagers.StudyGroupCollectionManager;
import Server.network.ServerRequestHandler;
import Server.network.TCPServer;
import Server.services.AuthService;


public class Server {
    public static void main(String[] args) {
        try {
            StudyGroupCollectionManager collectionManager = new StudyGroupCollectionManager();
            AuthService authService = new AuthService();
            ServerRequestHandler requestHandler = new ServerRequestHandler(collectionManager, authService);
            TCPServer server = new TCPServer(55555, requestHandler, 4, 8, 4); // Порт 5555
      //      TCPServer server = new TCPServer(55555, requestHandler); // Порт 5555

            StudyGroupExample example = new StudyGroupExample();
            StudyGroup studyGroupEx = example.StudyGroupExample();
            collectionManager.add(studyGroupEx);

            try {
                server.start();
            } catch (IOException e) {
                System.err.println("Ошибка запуска сервера: " + e.getMessage());
            }
        } catch (Exception e) { // Ловим все исключения
            System.err.println("Критическая ошибка: " + e.getMessage());
            System.exit(1);
        }

    }

}