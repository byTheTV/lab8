package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.RemoveLowerRequest;
import Common.responses.RemoveLowerResponse;

/**
 * Команда remove_lower: удаляет из коллекции все элементы, меньшие, чем заданный.
 * Ожидается, что аргумент команды является объектом Integer.
 */
public class RemoveLower extends NetworkCommand {

    public RemoveLower(TCPClient tcpClient, User user) {
        super(true, tcpClient, user);
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
        try {
            Long id = Long.parseLong(getArgument());
            RemoveLowerRequest request = new RemoveLowerRequest(id, user.getLogin(), user.getPassword());
            RemoveLowerResponse response = (RemoveLowerResponse) sendAndReceive(request);

            if (response != null) {
                if (response.getError() == null) {
                    System.out.println("Элементы, меньшие чем элемент с id " + id + ", успешно удалены");
                } else {
                    System.out.println("Ошибка: " + response.getError());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом");
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        if (argument == null) {
            System.out.println("Ошибка: id не указан");
            return false;
        }
        try {
            Long.parseLong((String) argument);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом");
            return false;
        }
    }
} 
 