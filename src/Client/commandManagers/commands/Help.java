package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.HelpRequest;
import Common.responses.HelpResponse;

/**
 * Команда help выводит справку по доступным командам.
 */
public class Help extends NetworkCommand {
    public Help(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescr() {
        return "вывести справку по доступным командам";
    }

    @Override
    public void execute() {
        HelpRequest request = new HelpRequest(user.getLogin(), user.getPassword());
        HelpResponse response = (HelpResponse) sendAndReceive(request);

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