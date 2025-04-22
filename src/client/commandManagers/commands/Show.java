package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.ShowRequest;
import Common.responses.ShowResponse;
import Common.models.Movie;

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
        ShowRequest request = new ShowRequest();
        ShowResponse response = (ShowResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                for (Movie movie : response.getMovies()) {
                    System.out.println(movie);
                }
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return argument == null;
    }
} 