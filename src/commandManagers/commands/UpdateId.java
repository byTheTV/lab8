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
        Long id = null;

        if (argument == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        try {
            id = Long.parseLong((String) argument);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Argument is not a valid long value", e);
        }

        try {
            StudyGroup oldGroup = collectionManager.getById(id);
            if (oldGroup == null) {
                System.out.println("Элемент с таким id не найден");
                return;
            }

            StudyGroup newGroup = new StudyGroup();
            newGroup.setId(oldGroup.getId());

            if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                System.out.print("Введите название группы (текущее: " + oldGroup.getName() + ") or (null чтобы оставить значение) > ");
            }
            String nameInput = commandManager.getScanner().nextLine().trim();
            if (!nameInput.isEmpty()) {
                newGroup.setName(nameInput);
            }


            newGroup.setCoordinates(readCoordinates(oldGroup.getCoordinates()));
            newGroup.setStudentsCount(readStudentsCount(oldGroup.getStudentsCount()));
            newGroup.setExpelledStudents(readExpelledStudents(oldGroup.getExpelledStudents()));
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

    private Coordinates readCoordinates(Coordinates current) {
        Long x = current.getX();
        Long y = current.getY();

        // Ввод X
        while (true) {
            try {
                System.out.print("Введите координату X (макс. 648, текущее: " + x + ") или нажмите Enter, чтобы оставить > ");
                String xInput = commandManager.getScanner().nextLine().trim();
                if (xInput.isEmpty()) {
                    break; // Оставляем текущее значение X
                }
                x = Long.parseLong(xInput);
                if (x > 648) {
                    System.out.println("Ошибка! X не может быть больше 648");
                    continue;
                }
                break; // Успешный ввод
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число для X");
            }
        }

        // Ввод Y
        while (true) {
            try {
                System.out.print("Введите координату Y (текущее: " + y + ") или нажмите Enter, чтобы оставить > ");
                String yInput = commandManager.getScanner().nextLine().trim();
                if (yInput.isEmpty()) {
                    break; // Оставляем текущее значение Y
                }
                y = Long.parseLong(yInput);
                break; // Успешный ввод
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число для Y");
            }
        }

        return new Coordinates(x, y);
    }

    private long readStudentsCount(long currentCount) {
        while (true) {
            try {
                System.out.print("Введите количество студентов (текущее: " + currentCount + ") или нажмите Enter, чтобы оставить > ");
                String input = commandManager.getScanner().nextLine().trim();
                if (input.isEmpty()) {
                    return currentCount; // Сохраняем текущее значение
                }
                long count = Long.parseLong(input);
                if (count <= 0) {
                    System.out.println("Количество должно быть больше 0");
                    continue;
                }
                return count; // Успешный ввод
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число");
            }
        }
    }

    private int readExpelledStudents(int currentExpelled) {
        while (true) {
            try {
                if (commandManager.getCurrentMode() == CommandMode.CLI_UserMode) {
                    System.out.print("Введите количество отчисленных студентов (текущее: " + currentExpelled + ")  or (null чтобы оставить значение)> ");
                }
                String input = commandManager.getScanner().nextLine().trim();
                if (input.isEmpty()) {
                    return currentExpelled;
                }
                int count = Integer.parseInt(input);

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