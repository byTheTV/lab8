package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.RemoveHeadRequest;
import Common.responses.Response;

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
        Response response = sendAndReceive(new RemoveHeadRequest());
        if (response != null) {
            if (response.getError() != null) {
                System.out.println("Ошибка: " + response.getError());
            } else {
                System.out.println(response.toString());
            }
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 