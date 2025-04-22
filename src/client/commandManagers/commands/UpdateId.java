package Client.commandManagers.commands;

import Client.commandManagers.*;
import Client.network.TCPClient;
import Common.models.*;
import Common.requests.UpdateIdRequest;
import Common.responses.Response;
import java.util.Scanner;

public class UpdateId extends NetworkCommand {
    private final CommandManager commandManager;
    private final InputReader inputReader;

    public UpdateId(Scanner scanner, CommandManager commandManager, TCPClient tcpClient) {
        super(true, tcpClient);
        this.commandManager = commandManager;
        this.inputReader = new InputReader(scanner, commandManager.getCurrentMode());
    }

    @Override
    public String getName() {
        return "update_id";
    }

    @Override
    public String getDescr() {
        return "обновить значение элемента коллекции по id";
    }

    @Override
    public void execute() {
        if (argument == null) {
            throw new IllegalArgumentException("Аргумент не может быть null");
        }

        long id = -1;
        try {
            id = Long.parseLong((String) argument);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Аргумент должен быть числом", e);
        }

        try {
            StudyGroup newGroup = new StudyGroup();
            newGroup.setId(id);

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setName(inputReader.readName(null)), "название группы");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setCoordinates(inputReader.readCoordinates(null)), "координаты");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setStudentsCount(inputReader.readStudentsCount(null)), "количество студентов");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setExpelledStudents(inputReader.readExpelledStudents(null)), "отчисленные студенты");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setTransferredStudents(inputReader.readTransferredStudents(null)), "переведенные студенты");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setFormOfEducation(inputReader.readFormOfEducation(null)), "форма обучения");

            inputReader.SetFieldWithRetry(newGroup, () -> newGroup.setGroupAdmin(inputReader.readGroupAdmin(null)), "администратор группы");

            Response response = sendAndReceive(new UpdateIdRequest(id, newGroup));
            if (response != null) {
                if (response.getError() != null) {
                    System.out.println("Ошибка: " + response.getError());
                } else {
                    System.out.println("Элемент успешно обновлен");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                execute();
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        try {
            Long.parseLong((String) argument);
            return true;
        } catch (NumberFormatException | ClassCastException e) {
            return false;
        }
    }
}