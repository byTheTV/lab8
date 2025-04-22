package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.ClearRequest;
import Common.responses.Response;

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
        Response response = sendAndReceive(new ClearRequest());
        if (response != null) {
            if (response.getError() != null) {
                System.out.println("Ошибка: " + response.getError());
            } else {
                System.out.println("Коллекция успешно очищена");
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 