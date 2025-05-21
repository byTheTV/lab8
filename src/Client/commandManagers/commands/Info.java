package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.InfoRequest;
import Common.responses.InfoResponse;

public class Info extends NetworkCommand {
    public Info(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
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
        InfoRequest request = new InfoRequest(user.getLogin(), user.getPassword());
        InfoResponse response = (InfoResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println(response.toString());
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