package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.RemoveHeadRequest;
import Common.responses.RemoveHeadResponse;

/**
 * Команда remove_head: выводит первый элемент коллекции и удаляет его.
 */
public class RemoveHead extends NetworkCommand {

    public RemoveHead(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
    }
    
    @Override
    public String getName() {
        return "remove_head";
    }
    
    @Override
    public String getDescr() {
        return "удалить первый элемент коллекции";
    }
    
    @Override
    public void execute() {
        RemoveHeadRequest request = new RemoveHeadRequest(user.getLogin(), user.getPassword());
        RemoveHeadResponse response = (RemoveHeadResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Первый элемент успешно удален");
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