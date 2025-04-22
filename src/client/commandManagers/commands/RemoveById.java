package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.RemoveByIdRequest;
import Common.responses.Response;

public class RemoveById extends NetworkCommand {
    public RemoveById(TCPClient tcpClient) {
        super(true, tcpClient);
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescr() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute() {
        try {
            long id = Long.parseLong((String) argument);
            Response response = sendAndReceive(new RemoveByIdRequest(id));
            if (response != null) {
                if (response.getError() != null) {
                    System.out.println("Ошибка: " + response.getError());
                } else {
                    System.out.println("Элемент успешно удален");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом");
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        try {
            Long.parseLong((String) argument);
            return true;
        } catch (NumberFormatException | ClassCastException e) {
            return false;
        }
    }
} 