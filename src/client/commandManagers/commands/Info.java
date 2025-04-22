package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.InfoRequest;
import Common.responses.InfoResponse;

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
        InfoRequest request = new InfoRequest();
        InfoResponse response = (InfoResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Тип коллекции: " + response.getCollectionType());
                System.out.println("Количество элементов: " + response.getSize());
                System.out.println("Дата инициализации: " + response.getCreationDate());
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