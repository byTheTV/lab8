package Client.commandManagers;

import Client.network.TCPClient;
import Common.models.User;
import Common.requests.Request;
import Common.responses.Response;

public abstract class NetworkCommand extends Command {
    protected final TCPClient tcpClient;
    protected final User user;

    public NetworkCommand(boolean hasArgument, TCPClient tcpClient, User user) {
        super(hasArgument);
        this.tcpClient = tcpClient;
        this.user = user;
    }

    protected Response sendAndReceive(Request request) {
        try {
            request.setUid(user.getUid());
            tcpClient.sendRequest(request);
            System.out.println("Отправлена команда");
            System.out.println(request.getName());
            return tcpClient.receiveResponse();
        } catch (Exception e) {
            System.err.println("Ошибка при отправке/получении данных: " + e.getMessage());
            return null;
        }
    }
} 