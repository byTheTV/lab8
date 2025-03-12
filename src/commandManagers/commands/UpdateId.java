package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;
import commandManagers.CommandMode;
import commandManagers.InputReader;
import collectionManagers.StudyGroupCollectionManager;
import models.*;
import java.util.Scanner;
import java.time.LocalDate;

public class UpdateId extends Command {
    private final CommandManager commandManager;
    private final InputReader inputReader;

    public UpdateId(StudyGroupCollectionManager collectionManager, Scanner scanner, CommandManager commandManager) {
        super(true, collectionManager);
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

        Long id;
        try {
            id = Long.parseLong((String) argument);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Аргумент должен быть числом", e);
        }

        try {
            StudyGroup oldGroup = collectionManager.getById(id);
            if (oldGroup == null) {
                System.out.println("Элемент с таким id не найден");
                return;
            }

            StudyGroup newGroup = new StudyGroup();
            newGroup.setId(oldGroup.getId());

            String nameInput = inputReader.readName();
            newGroup.setName(nameInput.isEmpty() ? oldGroup.getName() : nameInput);


            SetFieldWithRetry(newGroup, () -> newGroup.setCoordinates(inputReader.readCoordinates()), "координаты");

            SetFieldWithRetry(newGroup, () -> newGroup.setStudentsCount(inputReader.readStudentsCount()), "количество студентов");

            SetFieldWithRetry(newGroup, () -> newGroup.setExpelledStudents(inputReader.readExpelledStudents()), "отчисленные студенты");

            SetFieldWithRetry(newGroup, () -> newGroup.setTransferredStudents(inputReader.readTransferredStudents()), "переведенные студенты");

            SetFieldWithRetry(newGroup, () -> newGroup.setFormOfEducation(inputReader.readFormOfEducation()), "форма обучения");

            SetFieldWithRetry(newGroup, () -> newGroup.setGroupAdmin(inputReader.readGroupAdmin()), "администратор группы");

            collectionManager.updateById(id.intValue(), newGroup);
            System.out.println("Элемент успешно обновлен");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                execute(); // Повторный вызов в случае ошибки в CLI
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
        try {
            Long.parseLong((String) argument);
            return true;
        } catch (NumberFormatException | ClassCastException e) {
            return false;
        }
    }
}