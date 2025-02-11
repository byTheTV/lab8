package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;
import models.*;
import java.util.Scanner;
import java.time.LocalDate;

public class Add extends Command {
    private final Scanner scanner;

    public Add(StudyGroupCollectionManager collectionManager, Scanner scanner) {
        super(false, collectionManager);
        this.scanner = scanner;
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
            
            System.out.print("Введите название группы > ");
            studyGroup.setName(scanner.nextLine().trim());
            
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
        }
    }

    private Coordinates readCoordinates() {
        while (true) {
            try {
                System.out.print("Введите координату X (максимальное значение 648) > ");
                Long x = Long.parseLong(scanner.nextLine().trim());
                
                System.out.print("Введите координату Y > ");
                Long y = Long.parseLong(scanner.nextLine().trim());
                
                return new Coordinates(x, y);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректные числа");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private long readStudentsCount() {
        while (true) {
            try {
                System.out.print("Введите количество студентов > ");
                long count = Long.parseLong(scanner.nextLine().trim());
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
                int count = Integer.parseInt(scanner.nextLine().trim());
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
                int count = Integer.parseInt(scanner.nextLine().trim());
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
                
                String input = scanner.nextLine().trim();
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
            admin.setName(scanner.nextLine().trim());
            
            System.out.println("Введите дату рождения (в формате YYYY-MM-DD или пустую строку) > ");
            String birthdayStr = scanner.nextLine().trim();
            if (!birthdayStr.isEmpty()) {
                admin.setBirthday(LocalDate.parse(birthdayStr));
            }
            
            System.out.println("Введите номер паспорта (не более 26 символов или пустую строку) > ");
            String passportID = scanner.nextLine().trim();
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
                
                String input = scanner.nextLine().trim();
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
                Float x = Float.parseFloat(scanner.nextLine().trim());
                
                System.out.print("Введите координату Y локации > ");
                Float y = Float.parseFloat(scanner.nextLine().trim());
                
                System.out.print("Введите координату Z локации > ");
                Float z = Float.parseFloat(scanner.nextLine().trim());
                
                return new Location(x, y, z);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректные числа");
            }
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 