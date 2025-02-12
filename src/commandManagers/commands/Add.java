package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;
import commandManagers.CommandMode;
import collectionManagers.StudyGroupCollectionManager;
import models.*;
import java.util.Scanner;
import java.time.LocalDate;

public class Add extends Command {
    private final CommandManager commandManager;

    public Add(StudyGroupCollectionManager collectionManager, Scanner scanner, CommandManager commandManager) {
        super(false, collectionManager);
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
            
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.print("Введите название группы > ");
            }
            studyGroup.setName(commandManager.getScanner().nextLine().trim());
            
            studyGroup.setCoordinates(readCoordinates());
            studyGroup.setStudentsCount(readStudentsCount());
            studyGroup.setExpelledStudents(readExpelledStudents());
            studyGroup.setTransferredStudents(readTransferredStudents());
            studyGroup.setFormOfEducation(readFormOfEducation());
            studyGroup.setGroupAdmin(readGroupAdmin());

            collectionManager.add(studyGroup);
            System.out.println("Элемент успешно добавлен в коллекцию");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                execute();
            }
        }
    }

    private Coordinates readCoordinates() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите координату X (максимальное значение 648) > ");
                }
                Long x = Long.parseLong(commandManager.getScanner().nextLine().trim());
                
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите координату Y > ");
                }
                Long y = Long.parseLong(commandManager.getScanner().nextLine().trim());
                
                return new Coordinates(x, y);
            } catch (NumberFormatException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! Введите корректные числа");
                    continue;
                }
                throw e;
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println(e.getMessage());
                    continue;
                }
                throw e;
            }
        }
    }

    private long readStudentsCount() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите количество студентов > ");
                }
                long count = Long.parseLong(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (NumberFormatException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! Введите корректное число");
                    continue;
                }
                throw e;
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println(e.getMessage());
                    continue;
                }
                throw e;
            }
        }
    }

    private int readExpelledStudents() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите количество отчисленных студентов > ");
                }
                int count = Integer.parseInt(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! " + e.getMessage());
                    continue;
                }
                throw e;
            }
        }
    }

    private int readTransferredStudents() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите количество переведенных студентов > ");
                }
                int count = Integer.parseInt(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! " + e.getMessage());
                    continue;
                }
                throw e;
            }
        }
    }

    private FormOfEducation readFormOfEducation() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Выберите форму обучения:");
                    for (FormOfEducation form : FormOfEducation.values()) {
                        System.out.println(form.ordinal() + 1 + " — " + form);
                    }
                    System.out.print("> ");
                }
                
                String input = commandManager.getScanner().nextLine().trim();
                try {
                    int choice = Integer.parseInt(input);
                    if (choice > 0 && choice <= FormOfEducation.values().length) {
                        return FormOfEducation.values()[choice - 1];
                    }
                } catch (NumberFormatException ignored) {}
                
                return FormOfEducation.valueOf(input);
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! Выберите существующий вариант");
                    continue;
                }
                throw e;
            }
        }
    }

    private Person readGroupAdmin() {
        try {
            Person admin = new Person();
            
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.print("Введите имя администратора > ");
            }
            admin.setName(commandManager.getScanner().nextLine().trim());
            
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.println("Введите дату рождения (в формате YYYY-MM-DD или пустую строку) > ");
            }
            String birthdayStr = commandManager.getScanner().nextLine().trim();
            if (!birthdayStr.isEmpty()) {
                admin.setBirthday(LocalDate.parse(birthdayStr));
            }
            
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.println("Введите номер паспорта (не более 26 символов или пустую строку) > ");
            }
            String passportID = commandManager.getScanner().nextLine().trim();
            if (!passportID.isEmpty()) {
                admin.setPassportID(passportID);
            }
            
            admin.setEyeColor(readEyeColor());
            admin.setLocation(readLocation());
            
            return admin;
        } catch (IllegalArgumentException e) {
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.println("Ошибка: " + e.getMessage());
                return readGroupAdmin();
            }
            throw e;
        }
    }

    private Color readEyeColor() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Выберите цвет глаз:");
                    for (Color color : Color.values()) {
                        System.out.println(color.ordinal() + 1 + " — " + color);
                    }
                    System.out.print("> ");
                }
                
                String input = commandManager.getScanner().nextLine().trim();
                try {
                    int choice = Integer.parseInt(input);
                    if (choice > 0 && choice <= Color.values().length) {
                        return Color.values()[choice - 1];
                    }
                } catch (NumberFormatException ignored) {}
                
                return Color.valueOf(input);
            } catch (IllegalArgumentException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! Выберите существующий цвет");
                    continue;
                }
                throw e;
            }
        }
    }

    private Location readLocation() {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите координату X локации > ");
                }
                Float x = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите координату Y локации > ");
                }
                Float y = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите координату Z локации > ");
                }
                Float z = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                return new Location(x, y, z);
            } catch (NumberFormatException e) {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка! Введите корректные числа");
                    continue;
                }
                throw e;
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 