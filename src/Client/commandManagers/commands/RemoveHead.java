package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.StudyGroup;
import Common.requests.RemoveHeadRequest;
import Common.responses.RemoveHeadResponse;

/**
 * Команда remove_head: выводит первый элемент коллекции и удаляет его.
 */
public class RemoveHead extends NetworkCommand {

    public RemoveHead(TCPClient tcpClient) {
        super(false, tcpClient);
    }
    
    @Override
    public String getName() {
        return "remove_head";
    }
    
    @Override
    public String getDescr() {
        return "вывести первый элемент коллекции и удалить его";
    }
    
    @Override
    public void execute() {
        RemoveHeadRequest request = new RemoveHeadRequest();
        RemoveHeadResponse response = (RemoveHeadResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                StudyGroup group = response.getStudyGroup();
                if (group != null) {
                    System.out.println("Удален первый элемент коллекции:");
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