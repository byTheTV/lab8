package Server;

import java.io.IOException;

import Common.models.StudyGroup;
import Common.models.StudyGroupExample;
import Server.collectionManagers.StudyGroupCollectionManager;
import Server.network.ServerRequestHandler;
import Server.network.TCPServer;

public class Server {
    public static void main(String[] args) {
        StudyGroupCollectionManager collectionManager = new StudyGroupCollectionManager();
        ServerRequestHandler requestHandler = new ServerRequestHandler(collectionManager);
        TCPServer server = new TCPServer(55555, requestHandler); // Порт 5555

        StudyGroupExample example = new StudyGroupExample();
        StudyGroup studyGroupEx = example.StudyGroupExample();
        collectionManager.add(studyGroupEx);

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Ошибка запуска сервера: " + e.getMessage());
        }
    }

}