package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.StudyGroup;
import Common.requests.HeadRequest;
import Common.responses.HeadResponse;

/**
 * Команда head: выводит первый элемент коллекции.
 */
public class Head extends NetworkCommand {

    public Head(TCPClient tcpClient) {
        super(false, tcpClient);
    }
    
    @Override
    public String getName() {
        return "head";
    }
    
    @Override
    public String getDescr() {
        return "вывести первый элемент коллекции";
    }
    
    @Override
    public void execute() {
        HeadRequest request = new HeadRequest();
        HeadResponse response = (HeadResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                StudyGroup group = response.getStudyGroup();
                if (group != null) {
                    System.out.println("Первый элемент коллекции:");
                    System.out.println(group);
                } else {
                    System.out.println("Коллекция пуста");
                }
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 