package Client.commandManagers;

import Client.network.TCPClient;
import Common.requests.Request;
import Common.responses.Response;

public abstract class NetworkCommand extends Command {
    protected final TCPClient tcpClient;

    public NetworkCommand(boolean hasArgument, TCPClient tcpClient) {
        super(hasArgument);
        this.tcpClient = tcpClient;
    }

    protected Response sendAndReceive(Request request) {
        try {
            tcpClient.sendRequest(request);
            return tcpClient.receiveResponse();
        } catch (Exception e) {
            System.err.println("Ошибка при отправке/получении данных: " + e.getMessage());
            return null;
        }
    }
} 