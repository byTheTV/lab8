package Client.commandManagers.commands;

import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.User;
import Common.requests.PrintFieldAscendingGroupAdminRequest;
import Common.responses.PrintFieldAscendingGroupAdminResponse;

/**
 * Команда print_field_ascending_group_admin: выводит значения поля groupAdmin всех элементов 
 * коллекции в порядке возрастания. Сортировка производится по имени администратора.
 */
public class PrintFieldAscendingGroupAdmin extends NetworkCommand {

    public PrintFieldAscendingGroupAdmin(TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
    }
    
    @Override
    public String getName() {
        return "print_field_ascending_group_admin";
    }
    
    @Override
    public String getDescr() {
        return "вывести значения поля groupAdmin всех элементов в порядке возрастания";
    }
    
    @Override
    public void execute() {
        PrintFieldAscendingGroupAdminRequest request = new PrintFieldAscendingGroupAdminRequest(user.getLogin(), user.getPassword());
        PrintFieldAscendingGroupAdminResponse response = (PrintFieldAscendingGroupAdminResponse) sendAndReceive(request);

        if (response != null) {
            if (response.getError() == null) {
                System.out.println("Имена администраторов групп в порядке возрастания:");
                response.getAdminNames().forEach(System.out::println);
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