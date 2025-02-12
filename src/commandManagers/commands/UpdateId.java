package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;
import commandManagers.CommandMode;
import collectionManagers.StudyGroupCollectionManager;
import models.*;
import java.time.LocalDate;

public class UpdateId extends Command {
    private final CommandManager commandManager;

    public UpdateId(StudyGroupCollectionManager collectionManager, CommandManager commandManager) {
        super(true, collectionManager);
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
            Long id = Long.parseLong((String) argument);
            StudyGroup oldGroup = collectionManager.getById(id);
            if (oldGroup == null) {
                System.out.println("Элемент с таким id не найден");
                return;
            }

            StudyGroup newGroup = new StudyGroup();
            newGroup.setId(oldGroup.getId());
            
            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.print("Введите название группы > ");
            }
            newGroup.setName(commandManager.getScanner().nextLine().trim());
            
            newGroup.setCoordinates(readCoordinates());
            newGroup.setStudentsCount(readStudentsCount());
            newGroup.setExpelledStudents(readExpelledStudents());
            newGroup.setTransferredStudents(readTransferredStudents());
            newGroup.setFormOfEducation(readFormOfEducation());
            newGroup.setGroupAdmin(readGroupAdmin());

            collectionManager.updateById(id.intValue(), newGroup);
            System.out.println("Элемент успешно обновлен");
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
            }
        }
    }

    private long readStudentsCount() {
        while (true) {
            try {
                System.out.print("Введите количество студентов > ");
                long count = Long.parseLong(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int readExpelledStudents() {
        while (true) {
            try {
                System.out.print("Введите количество отчисленных студентов > ");
                int count = Integer.parseInt(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int readTransferredStudents() {
        while (true) {
            try {
                System.out.print("Введите количество переведенных студентов > ");
                int count = Integer.parseInt(commandManager.getScanner().nextLine().trim());
                if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                return count;
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private FormOfEducation readFormOfEducation() {
        while (true) {
            try {
                System.out.println("Выберите форму обучения:");
                for (FormOfEducation form : FormOfEducation.values()) {
                    System.out.println(form.ordinal() + 1 + " — " + form);
                }
                System.out.print("> ");
                
                String input = commandManager.getScanner().nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= FormOfEducation.values().length) {
                    return FormOfEducation.values()[choice - 1];
                }
                return FormOfEducation.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Выберите существующий вариант");
            }
        }
    }

    private Person readGroupAdmin() {
        try {
            Person admin = new Person();
            
            System.out.print("Введите имя администратора > ");
            admin.setName(commandManager.getScanner().nextLine().trim());
            
            System.out.println("Введите дату рождения (в формате YYYY-MM-DD или пустую строку) > ");
            String birthdayStr = commandManager.getScanner().nextLine().trim();
            if (!birthdayStr.isEmpty()) {
                admin.setBirthday(LocalDate.parse(birthdayStr));
            }
            
            System.out.println("Введите номер паспорта (не более 26 символов или пустую строку) > ");
            String passportID = commandManager.getScanner().nextLine().trim();
            if (!passportID.isEmpty()) {
                admin.setPassportID(passportID);
            }
            
            admin.setEyeColor(readEyeColor());
            admin.setLocation(readLocation());
            
            return admin;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
            return readGroupAdmin();
        }
    }

    private Color readEyeColor() {
        while (true) {
            try {
                System.out.println("Выберите цвет глаз:");
                for (Color color : Color.values()) {
                    System.out.println(color.ordinal() + 1 + " — " + color);
                }
                System.out.print("> ");
                
                String input = commandManager.getScanner().nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= Color.values().length) {
                    return Color.values()[choice - 1];
                }
                return Color.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка! Выберите существующий цвет");
            }
        }
    }

    private Location readLocation() {
        while (true) {
            try {
                System.out.print("Введите координату X локации > ");
                Float x = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                System.out.print("Введите координату Y локации > ");
                Float y = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                System.out.print("Введите координату Z локации > ");
                Float z = Float.parseFloat(commandManager.getScanner().nextLine().trim());
                
                return new Location(x, y, z);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректные числа");
            }
        }
    }
} 