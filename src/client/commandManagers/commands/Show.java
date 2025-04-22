package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.ShowRequest;
import Common.responses.Response;

public class Show extends NetworkCommand {
    public Show(TCPClient tcpClient) {
        super(false, tcpClient);
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescr() {
        return "вывести все элементы коллекции";
    }

    @Override
    public void execute() {
        Response response = sendAndReceive(new ShowRequest());
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