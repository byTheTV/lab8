package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.HeadRequest;
import Common.responses.Response;

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
        Response response = sendAndReceive(new HeadRequest());
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