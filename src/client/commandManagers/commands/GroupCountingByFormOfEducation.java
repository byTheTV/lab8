package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Common.requests.GroupCountingByFormOfEducationRequest;
import Common.responses.GroupCountingByFormOfEducationResponse;
import Client.network.TCPClient;

/**
 * Команда group_counting_by_form_of_education: группирует элементы коллекции по значению поля formOfEducation
 * и выводит количество элементов в каждой группе.
 */
public class GroupCountingByFormOfEducation extends NetworkCommand {

    public GroupCountingByFormOfEducation(TCPClient tcpClient) {
        super(false, tcpClient);
    }
    
    @Override
    public String getName() {
        return "group_counting_by_form_of_education";
    }
    
    @Override
    public String getDescr() {
        return "сгруппировать элементы коллекции по значению поля formOfEducation, вывести количество элементов в каждой группе";
    }
    
    @Override
    public void execute() {
        GroupCountingByFormOfEducationRequest request = new GroupCountingByFormOfEducationRequest();
        GroupCountingByFormOfEducationResponse response = (GroupCountingByFormOfEducationResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Количество групп по формам обучения:");
                response.getCounts().forEach((form, count) -> 
                    System.out.println(form + ": " + count));
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