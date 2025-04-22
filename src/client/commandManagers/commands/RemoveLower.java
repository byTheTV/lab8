package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.StudyGroup;
import Common.requests.RemoveLowerRequest;
import Common.responses.Response;

/**
 * Команда remove_lower: удаляет из коллекции все элементы, меньшие, чем заданный.
 * Ожидается, что аргумент команды является объектом StudyGroup.
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
        if (argument == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        try {
            long id = Long.parseLong((String) argument);
            Response response = sendAndReceive(new RemoveLowerRequest(id));
            if (response != null) {
                if (response.getError() != null) {
                    System.out.println("Ошибка: " + response.getError());
                } else {
                    System.out.println(response.toString());
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Argument is not a valid long value", e);
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof StudyGroup;
    }
} 