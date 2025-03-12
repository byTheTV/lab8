package commandManagers;

import collectionManagers.StudyGroupCollectionManager;
import exceptions.BuildObjectException;
import models.*;

import java.time.LocalDate;
import java.util.Scanner;

/**
 Abstract class representing a command that can be executed in the program.
 */
public abstract class Command implements CommandInterface {

    /**
     Boolean indicating whether this command requires an argument or not.
     */
    private final boolean hasArgument;

    /**
     The argument for this command.
     */
    protected String argument;

    /**
     The collection manager for this command.
     */
    protected StudyGroupCollectionManager collectionManager;

    /**
     * Base method for show command name
     *
     * @return command name
     */
    @Override
    public abstract String getName();

    /**
     * Base method for show command description.
     *
     * @return command description
     */
    @Override
    public abstract String getDescr();

    /**
     Constructs a new command with the specified argument requirement and collection manager.
     @param hasArgument a boolean indicating whether this command requires an argument or not.
     @param collectionManager the collection manager for this command.
     */
    public Command(boolean hasArgument, StudyGroupCollectionManager collectionManager) {
        this.hasArgument = hasArgument;
        this.collectionManager = collectionManager;
    }



    /**
     Constructs a new command with the specified argument requirement.
     @param hasArgument a boolean indicating whether this command requires an argument or not.
     */
    public Command(boolean hasArgument) {
        this.hasArgument = hasArgument;
        this.collectionManager = null;
    }

    /**
     Executes command.
     */
    @Override
    public abstract void execute() throws BuildObjectException;

    /**
     Checks if the given inputArgument is valid for this command.
     @param inputArgument the argument to check.
     @return true if the argument is valid, false otherwise.
     */
    @Override
    public abstract boolean checkArgument(Object inputArgument);

    /**
     Returns whether this command requires an argument.
     @return true if this command requires an argument, false otherwise.
     */
    public boolean isHasArgument() {
        return hasArgument;
    }

    /**
     Returns the argument for this command.
     @return the argument for this command.
     */
    public String getArgument() {
        return argument;
    }

    /**
     Sets the argument for this command.
     @param argument the argument to set.
     */
    public void setArgument(String argument) {
        this.argument = argument;
    }

    public static class InputReader {
        private final Scanner scanner;
        private final CommandMode mode;

        public InputReader(Scanner scanner, CommandMode mode) {
            this.scanner = scanner;
            this.mode = mode;
        }

        public String readName() {
            if (mode == CommandMode.CLI_UserMode) {
                System.out.print("Введите название группы > ");
            }
            return scanner.nextLine().trim();
        }

        public Coordinates readCoordinates() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите координату X (максимальное значение 648) > ");
                    }
                    Long x = Long.parseLong(scanner.nextLine().trim());
                    if (x > 648) throw new IllegalArgumentException("X не может быть больше 648");

                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите координату Y > ");
                    }
                    Long y = Long.parseLong(scanner.nextLine().trim());

                    return new Coordinates(x, y);
                } catch (NumberFormatException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Введите корректные числа");
                    } else {
                        throw e;
                    }
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println(e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        }

        public long readStudentsCount() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите количество студентов > ");
                    }
                    long count = Long.parseLong(scanner.nextLine().trim());
                    if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                    return count;
                } catch (NumberFormatException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Введите корректное число");
                    } else {
                        throw e;
                    }
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println(e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        }

        public int readExpelledStudents() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите количество отчисленных студентов > ");
                    }
                    int count = Integer.parseInt(scanner.nextLine().trim());
                    if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                    return count;
                } catch (NumberFormatException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Введите корректное число");
                    } else {
                        throw e;
                    }
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println(e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        }

        public int readTransferredStudents() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите количество переведенных студентов > ");
                    }
                    int count = Integer.parseInt(scanner.nextLine().trim());
                    if (count <= 0) throw new IllegalArgumentException("Количество должно быть больше 0");
                    return count;
                } catch (NumberFormatException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Введите корректное число");
                    } else {
                        throw e;
                    }
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println(e.getMessage());
                    } else {
                        throw e;
                    }
                }
            }
        }

        public FormOfEducation readFormOfEducation() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Выберите форму обучения:");
                        for (FormOfEducation form : FormOfEducation.values()) {
                            System.out.println(form.ordinal() + 1 + " — " + form);
                        }
                        System.out.print("> ");
                    }
                    String input = scanner.nextLine().trim();
                    try {
                        int choice = Integer.parseInt(input);
                        if (choice > 0 && choice <= FormOfEducation.values().length) {
                            return FormOfEducation.values()[choice - 1];
                        }
                    } catch (NumberFormatException ignored) {}
                    return FormOfEducation.valueOf(input);
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Выберите существующий вариант");
                    } else {
                        throw e;
                    }
                }
            }
        }

        public Person readGroupAdmin() {
            try {
                Person admin = new Person();
                if (mode == CommandMode.CLI_UserMode) {
                    System.out.print("Введите имя администратора > ");
                }
                admin.setName(scanner.nextLine().trim());

                if (mode == CommandMode.CLI_UserMode) {
                    System.out.println("Введите дату рождения (в формате YYYY-MM-DD или пустую строку) > ");
                }
                String birthdayStr = scanner.nextLine().trim();
                if (!birthdayStr.isEmpty()) {
                    admin.setBirthday(LocalDate.parse(birthdayStr));
                }

                if (mode == CommandMode.CLI_UserMode) {
                    System.out.println("Введите номер паспорта (не более 26 символов или пустую строку) > ");
                }
                String passportID = scanner.nextLine().trim();
                if (!passportID.isEmpty()) {
                    admin.setPassportID(passportID);
                }

                admin.setEyeColor(readEyeColor());
                admin.setLocation(readLocation());

                return admin;
            } catch (IllegalArgumentException e) {
                if (mode == CommandMode.CLI_UserMode) {
                    System.out.println("Ошибка: " + e.getMessage());
                    return readGroupAdmin(); // Рекурсивный вызов для повторного ввода
                } else {
                    throw e;
                }
            }
        }

        public Color readEyeColor() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Выберите цвет глаз:");
                        for (Color color : Color.values()) {
                            System.out.println(color.ordinal() + 1 + " — " + color);
                        }
                        System.out.print("> ");
                    }
                    String input = scanner.nextLine().trim();
                    try {
                        int choice = Integer.parseInt(input);
                        if (choice > 0 && choice <= Color.values().length) {
                            return Color.values()[choice - 1];
                        }
                    } catch (NumberFormatException ignored) {}
                    return Color.valueOf(input);
                } catch (IllegalArgumentException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Выберите существующий цвет");
                    } else {
                        throw e;
                    }
                }
            }
        }

        public Location readLocation() {
            while (true) {
                try {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите координату X локации > ");
                    }
                    Float x = Float.parseFloat(scanner.nextLine().trim());

                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите координату Y локации > ");
                    }
                    Float y = Float.parseFloat(scanner.nextLine().trim());

                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.print("Введите координату Z локации > ");
                    }
                    Float z = Float.parseFloat(scanner.nextLine().trim());

                    return new Location(x, y, z);
                } catch (NumberFormatException e) {
                    if (mode == CommandMode.CLI_UserMode) {
                        System.out.println("Ошибка! Введите корректные числа");
                    } else {
                        throw e;
                    }
                }
            }
        }
    }
}
