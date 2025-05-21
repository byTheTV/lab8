package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.ShowRequest;
import Common.responses.ShowResponse;

public class Show extends NetworkCommand {
    public Show(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
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
        ShowRequest request = new ShowRequest(user.getLogin(), user.getPassword());
        ShowResponse response = (ShowResponse) sendAndReceive(request);

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