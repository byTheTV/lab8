package Client.commandManagers.commands;

import java.util.Scanner;

import Client.commandManagers.NetworkCommand;
import Client.commandManagers.CommandManager;
import Client.commandManagers.CommandMode;
import Client.commandManagers.InputReader;
import Common.models.StudyGroup;
import Common.requests.AddRequest;
import Client.network.TCPClient;
import Common.responses.AddResponse;


public class Add extends NetworkCommand {
    private final CommandManager commandManager;
    private final InputReader inputReader;

    public Add(Scanner scanner, CommandManager commandManager, TCPClient tcpClient) {
        super(false, tcpClient);
        this.commandManager = commandManager;
        this.inputReader = new InputReader(scanner, commandManager.getCurrentMode());
    }

    @Override
    public String getName() {
        return "add ";
    }

    @Override
    public String getDescr() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public void execute() {
        try {
            StudyGroup studyGroup = new StudyGroup();

            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setName(inputReader.readName(null)), "название группы");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setCoordinates(inputReader.readCoordinates(null)), "координаты");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setStudentsCount(inputReader.readStudentsCount(null)), "количество студентов");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setExpelledStudents(inputReader.readExpelledStudents(null)), "отчисленные студенты");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setTransferredStudents(inputReader.readTransferredStudents(null)), "переведенные студенты");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setFormOfEducation(inputReader.readFormOfEducation(null)), "форма обучения");
            inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setGroupAdmin(inputReader.readGroupAdmin(null)), "администратор группы");

            AddRequest request = new AddRequest(studyGroup);
            AddResponse response = (AddResponse) sendAndReceive(request);

            if (response != null) {
                if (response.getError() == null) {
                    System.out.println("Элемент успешно добавлен в коллекцию");
                } else {
                    System.out.println("Ошибка: " + response.getError());
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
        return true;
    }
}

