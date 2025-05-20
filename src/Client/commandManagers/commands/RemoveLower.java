package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.RemoveLowerRequest;
import Common.responses.RemoveLowerResponse;

/**
 * Команда remove_lower: удаляет из коллекции все элементы, меньшие, чем заданный.
 * Ожидается, что аргумент команды является объектом Integer.
 */
public class RemoveLower extends NetworkCommand {

    public RemoveLower(TCPClient tcpClient) {
        super(true, tcpClient);
    }
    
    @Override
    public String getName() {
        return "remove_lower";
    }
    
    @Override
    public String getDescr() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
    
    @Override
    public void execute() {
        RemoveLowerRequest request = new RemoveLowerRequest(Long.parseLong(argument));
        RemoveLowerResponse response = (RemoveLowerResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Удалено элементов: " + response.getRemovedCount());
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof Integer;
    }
} 
 