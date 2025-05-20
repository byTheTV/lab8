package Client.commandManagers.commands;

import java.util.Scanner;

import Client.commandManagers.NetworkCommand;
import Client.commandManagers.CommandManager;
import Client.commandManagers.CommandMode;
import Client.commandManagers.InputReader;
import Common.models.StudyGroup;
import Common.requests.UpdateIdRequest;
import Common.responses.UpdateIdResponse;
import Client.network.TCPClient;

public class UpdateId extends NetworkCommand {
    private final CommandManager commandManager;

    public UpdateId(Scanner scanner, CommandManager commandManager, TCPClient tcpClient) {
        super(true, tcpClient);
        this.commandManager = commandManager;
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
        try {
            Long id = Long.parseLong((String) getArgument());
            StudyGroup studyGroup = new StudyGroup();
            InputReader inputReader = new InputReader(commandManager.getScanner(), commandManager.getCurrentMode());

            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setName(inputReader.readName(null)), "название группы");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setCoordinates(inputReader.readCoordinates(null)), "координаты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setStudentsCount(inputReader.readStudentsCount(null)), "количество студентов");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setExpelledStudents(inputReader.readExpelledStudents(null)), "отчисленные студенты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setTransferredStudents(inputReader.readTransferredStudents(null)), "переведенные студенты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setFormOfEducation(inputReader.readFormOfEducation(null)), "форма обучения");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setGroupAdmin(inputReader.readGroupAdmin(null)), "администратор группы");
            }else {
                studyGroup.setName(inputReader.readName(null));
                studyGroup.setCoordinates(inputReader.readCoordinates(null));
                studyGroup.setStudentsCount(inputReader.readStudentsCount(null));
                studyGroup.setExpelledStudents(inputReader.readExpelledStudents(null));
                studyGroup.setTransferredStudents(inputReader.readTransferredStudents(null));
                studyGroup.setFormOfEducation(inputReader.readFormOfEducation(null));
                studyGroup.setGroupAdmin(inputReader.readGroupAdmin(null));
            }

            UpdateIdRequest request = new UpdateIdRequest(id, studyGroup);
            UpdateIdResponse response = (UpdateIdResponse) sendAndReceive(request);

            if (response != null) {
                if (response.getError() == null) {
                    System.out.println("Элемент успешно обновлен");
                } else {
                    System.out.println("Ошибка: " + response.getError());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть целым числом");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                execute();
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        if (!(argument instanceof String)) {
            return false;
        }
        try {
            Integer.parseInt((String) argument);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}