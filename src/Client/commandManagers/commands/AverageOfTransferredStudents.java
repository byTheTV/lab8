package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.requests.AverageOfTransferredStudentsRequest;
import Common.responses.AverageOfTransferredStudentsResponse;

/**
 * Команда average_of_transferred_students: выводит среднее значение поля transferredStudents.
 */
public class AverageOfTransferredStudents extends NetworkCommand {

    public AverageOfTransferredStudents(TCPClient tcpClient) {
        super(false, tcpClient);
    }
    
    @Override
    public String getName() {
        return "average_of_transferred_students";
    }
    
    @Override
    public String getDescr() {
        return "вывести среднее значение поля transferredStudents для всех элементов коллекции";
    }
    
    @Override
    public void execute() {
        AverageOfTransferredStudentsRequest request = new AverageOfTransferredStudentsRequest();
        AverageOfTransferredStudentsResponse response = (AverageOfTransferredStudentsResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Среднее значение transferredStudents: " + response.getAverage());
            } else {
                System.out.println("Ошибка: " + response.getError());
            }
        }
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 