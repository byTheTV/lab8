package Client.commandManagers.commands;

import java.util.Scanner;

import Client.commandManagers.CommandManager;
import Client.commandManagers.CommandMode;
import Client.commandManagers.InputReader;
import Client.commandManagers.NetworkCommand;
import Client.network.TCPClient;
import Common.models.StudyGroup;
import Common.models.User;
import Common.requests.AddRequest;
import Common.responses.AddResponse;

public class Add extends NetworkCommand {
    private final CommandManager commandManager;

    public Add(Scanner scanner, CommandManager commandManager, TCPClient tcpClient, User user) {
        super(false, tcpClient, user);
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescr() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public void execute() {
        try {
            StudyGroup studyGroup = new StudyGroup();
            InputReader inputReader = new InputReader(commandManager.getScanner(), commandManager.getCurrentMode());

            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                // Интерактивный режим с повторными попытками
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setName(inputReader.readName(null)), "название группы");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setCoordinates(inputReader.readCoordinates(null)), "координаты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setStudentsCount(inputReader.readStudentsCount(null)), "количество студентов");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setExpelledStudents(inputReader.readExpelledStudents(null)), "отчисленные студенты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setTransferredStudents(inputReader.readTransferredStudents(null)), "переведенные студенты");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setFormOfEducation(inputReader.readFormOfEducation(null)), "форма обучения");
                inputReader.SetFieldWithRetry(studyGroup, () -> studyGroup.setGroupAdmin(inputReader.readGroupAdmin(null)), "администратор группы");
            } else {
                // Режим скрипта: чтение без повторных попыток
                studyGroup.setName(inputReader.readName(null));
                studyGroup.setCoordinates(inputReader.readCoordinates(null));
                studyGroup.setStudentsCount(inputReader.readStudentsCount(null));
                studyGroup.setExpelledStudents(inputReader.readExpelledStudents(null));
                studyGroup.setTransferredStudents(inputReader.readTransferredStudents(null));
                studyGroup.setFormOfEducation(inputReader.readFormOfEducation(null));
                studyGroup.setGroupAdmin(inputReader.readGroupAdmin(null));
            }

            AddRequest request = new AddRequest(studyGroup, user.getLogin(), user.getPassword());
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
                execute(); // Повтор только в CLI
            } else {
                throw e; // В скрипте прерываем выполнение
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true; // Команда add не принимает аргументов
    }
}