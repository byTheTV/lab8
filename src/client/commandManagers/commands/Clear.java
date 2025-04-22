package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.ClearRequest;
import Common.responses.ClearResponse;

public class Clear extends NetworkCommand {
    public Clear(TCPClient tcpClient) {
        super(false, tcpClient);
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescr() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        ClearRequest request = new ClearRequest();
        ClearResponse response = (ClearResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Коллекция успешно очищена");
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 