package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.HeadRequest;
import Common.responses.HeadResponse;

/**
 * Команда head: выводит первый элемент коллекции.
 */
public class Head extends NetworkCommand {

    public Head(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
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
        HeadRequest request = new HeadRequest(user.getLogin(), user.getPassword());
        HeadResponse response = (HeadResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println(response.toString());
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