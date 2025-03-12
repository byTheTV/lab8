package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;
import commandManagers.CommandMode;
import commandManagers.InputReader;
import collectionManagers.StudyGroupCollectionManager;
import models.*;
import java.util.Scanner;
import java.time.LocalDate;

public class Add extends Command {
    private final CommandManager commandManager;
    private final InputReader inputReader;

    public Add(StudyGroupCollectionManager collectionManager, Scanner scanner, CommandManager commandManager) {
        super(false, collectionManager);
        this.commandManager = commandManager;
        this.inputReader = new InputReader(scanner, commandManager.getCurrentMode());
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


            SetFieldWithRetry(studyGroup, () -> studyGroup.setName(inputReader.readName()), "название группы");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setCoordinates(inputReader.readCoordinates()), "координаты");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setStudentsCount(inputReader.readStudentsCount()), "количество студентов");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setExpelledStudents(inputReader.readExpelledStudents()), "отчисленные студенты");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setTransferredStudents(inputReader.readTransferredStudents()), "переведенные студенты");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setFormOfEducation(inputReader.readFormOfEducation()), "форма обучения");

            SetFieldWithRetry(studyGroup, () -> studyGroup.setGroupAdmin(inputReader.readGroupAdmin()), "администратор группы");

            collectionManager.add(studyGroup);
            System.out.println("Элемент успешно добавлен в коллекцию");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                execute();
            }
        }
    }

    public void SetFieldWithRetry(StudyGroup studyGroup, Runnable setter, String fieldName) {
        while (true) {
            try {
                setter.run();
                break; // Выходим из цикла, если setter выполнился успешно
            } catch (Exception e) {
                System.out.println("Ошибка в поле '" + fieldName + "': " + e.getMessage() + ". Попробуйте снова.");
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
}

