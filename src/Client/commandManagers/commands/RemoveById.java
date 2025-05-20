package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.RemoveByIdRequest;
import Common.responses.RemoveByIdResponse;

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
            Long id = Long.parseLong((String) getArgument());
            RemoveByIdRequest request = new RemoveByIdRequest(id);
            RemoveByIdResponse response = (RemoveByIdResponse) sendAndReceive(request);

            if (response != null) {
                if (response.getError() == null) {
                    System.out.println("Элемент успешно удален");
                } else {
                    System.out.println("Ошибка: " + response.getError());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть целым числом");
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        if (!(argument instanceof String)) {
            return false;
        }
        try {
            Long.parseLong((String) argument);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
} 