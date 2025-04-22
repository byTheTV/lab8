package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.InfoRequest;
import Common.responses.Response;

public class Info extends NetworkCommand {
    public Info(TCPClient tcpClient) {
        super(false, tcpClient);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescr() {
        return "вывести информацию о коллекции";
    }

    @Override
    public void execute() {
        Response response = sendAndReceive(new InfoRequest());
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
        return true;
    }
} 