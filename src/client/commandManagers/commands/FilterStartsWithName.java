package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.FilterStartsWithNameRequest;
import Common.responses.FilterStartsWithNameResponse;

public class FilterStartsWithName extends NetworkCommand {
    public FilterStartsWithName(TCPClient tcpClient) {
        super(true, tcpClient);
    }

    @Override
    public String getName() {
        return "filter_starts_with_name";
    }

    @Override
    public String getDescr() {
        return "вывести элементы, значение поля name которых начинается с заданной подстроки";
    }

    @Override
    public void execute() {
        String namePrefix = (String) getArgument();
        FilterStartsWithNameRequest request = new FilterStartsWithNameRequest(namePrefix);
        FilterStartsWithNameResponse response = (FilterStartsWithNameResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Найденные элементы:");
                response.getGroups().forEach(System.out::println);
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof String;
    }
} 