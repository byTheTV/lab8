package Client.commandManagers;

import Common.models.*;
import java.time.LocalDate;
import java.util.Scanner;

public class InputReader {
    private final Scanner scanner;
    private final CommandMode mode;

    public InputReader(Scanner scanner, CommandMode mode) {
        this.scanner = scanner;
        this.mode = mode;
    }

    public void SetFieldWithRetry(StudyGroup studyGroup, Runnable setter, String fieldName) {
        while (true) {
            try {
                setter.run();
                break;
            } catch (Exception e) {
                System.out.println("Ошибка в поле '" + fieldName + "': " + e.getMessage() + ". Попробуйте снова.");
            }
        }
    }

    public String readName(String defaultValue) {
        if (mode == CommandMode.CLI_UserMode) {
            String prompt = "Введите название группы";
            if (defaultValue != null) prompt += " (пробел для '" + defaultValue + "')";
            System.out.print(prompt + " > ");
        }
        String input = scanner.nextLine().trim();
        return (defaultValue != null && input.isEmpty()) ? defaultValue : input;
    }

    public Coordinates readCoordinates(Coordinates defaultValue) {
        while (true) {
            try {
                Long x, y;

                // Обработка X
                if (mode == CommandMode.CLI_UserMode) {
                    String xPrompt = "Введите координату X (максимальное значение 648)";
                    if (defaultValue != null) xPrompt += " (пробел для " + defaultValue.getX() + ")";
                    System.out.print(xPrompt + " > ");
                }
                String xInput = scanner.nextLine().trim();
                x = (defaultValue != null && xInput.isEmpty())
                        ? defaultValue.getX()
                        : Long.parseLong(xInput);
                if (x > 648) throw new IllegalArgumentException("X не может быть больше 648");

                // Обработка Y
                if (mode == CommandMode.CLI_UserMode) {
                    String yPrompt = "Введите координату Y";
                    if (defaultValue != null) yPrompt += " (пробел для " + defaultValue.getY() + ")";
                    System.out.print(yPrompt + " > ");
                }
                String yInput = scanner.nextLine().trim();
                y = (defaultValue != null && yInput.isEmpty())
                        ? defaultValue.getY()
                        : Long.parseLong(yInput);

                return new Coordinates(x, y);
            } catch (NumberFormatException e) {
                if (mode == CommandMode.CLI_UserMode) {
                    System.out.println(e.getMessage());
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


    public Long readStudentsCount(Long defaultValue) {
        while (true) {
            try {
                if (mode == CommandMode.CLI_UserMode) {
                    String prompt = "Введите количество студентов";
                    if (defaultValue != null) prompt += " (пробел для " + defaultValue + ")";
                    System.out.print(prompt + " > ");
                }
                String input = scanner.nextLine().trim();
                long count;
                if (defaultValue != null && input.isEmpty()) {
                    count = defaultValue; // Используем значение по умолчанию, если оно есть
                } else {
                    count = Long.parseLong(input); // Парсим введённое значение
                }
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

    public Integer readExpelledStudents(Integer defaultValue) {
        while (true) {
            try {
                if (mode == CommandMode.CLI_UserMode) {
                    String prompt = "Введите количество отчисленных студентов";
                    if (defaultValue != null) prompt += " (пробел для " + defaultValue + ")";
                    System.out.print(prompt + " > ");
                }
                String input = scanner.nextLine().trim();
                int count = (defaultValue != null && input.isEmpty())
                        ? defaultValue
                        : Integer.parseInt(input);
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

    public Integer readTransferredStudents(Integer defaultValue) {
        while (true) {
            try {
                if (mode == CommandMode.CLI_UserMode) {
                    String prompt = "Введите количество переведенных студентов";
                    if (defaultValue != null) prompt += " (пробел для " + defaultValue + ")";
                    System.out.print(prompt + " > ");
                }
                String input = scanner.nextLine().trim();
                int count = (defaultValue != null && input.isEmpty())
                        ? defaultValue
                        : Integer.parseInt(input);
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


    public FormOfEducation readFormOfEducation(FormOfEducation defaultValue) {
        while (true) {
            try {
                if (mode == CommandMode.CLI_UserMode) {
                    String prompt = "Выберите форму обучения";
                    if (defaultValue != null) prompt += " (пробел для " + defaultValue + "):";
                    else prompt += ":";
                    System.out.println(prompt);
                    for (FormOfEducation form : FormOfEducation.values()) {
                        System.out.println(form.ordinal() + 1 + " — " + form);
                    }
                    System.out.print("> ");
                }
                String input = scanner.nextLine().trim();
                if (defaultValue != null && input.isEmpty()) {
                    return defaultValue;
                }
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

    public Person readGroupAdmin(Person defaultValue) {
        try {
            Person admin = new Person();
            if (mode == CommandMode.CLI_UserMode) {
                String namePrompt = "Введите имя администратора";
                if (defaultValue != null && defaultValue.getName() != null) {
                    namePrompt += " (пробел для '" + defaultValue.getName() + "')";
                }
                System.out.print(namePrompt + " > ");
            }
            String nameInput = scanner.nextLine().trim();
            if (defaultValue != null && nameInput.isEmpty() && defaultValue.getName() != null) {
                admin.setName(defaultValue.getName());
            } else {
                admin.setName(nameInput);
            }

            if (mode == CommandMode.CLI_UserMode) {
                String birthdayPrompt = "Введите дату рождения (в формате YYYY-MM-DD или пустую строку)";
                if (defaultValue != null && defaultValue.getBirthday() != null) {
                    birthdayPrompt += " (пробел для " + defaultValue.getBirthday() + ")";
                }
                System.out.println(birthdayPrompt + " > ");
            }
            String birthdayStr = scanner.nextLine().trim();
            if (defaultValue != null && birthdayStr.isEmpty() && defaultValue.getBirthday() != null) {
                admin.setBirthday(defaultValue.getBirthday());
            } else if (!birthdayStr.isEmpty()) {
                admin.setBirthday(LocalDate.parse(birthdayStr));
            }

            if (mode == CommandMode.CLI_UserMode) {
                String passportPrompt = "Введите номер паспорта (не более 26 символов или пустую строку)";
                if (defaultValue != null && defaultValue.getPassportID() != null) {
                    passportPrompt += " (пробел для '" + defaultValue.getPassportID() + "')";
                }
                System.out.println(passportPrompt + " > ");
            }
            String passportID = scanner.nextLine().trim();
            if (defaultValue != null && passportID.isEmpty() && defaultValue.getPassportID() != null) {
                admin.setPassportID(defaultValue.getPassportID());
            } else if (!passportID.isEmpty()) {
                admin.setPassportID(passportID);
            }

            admin.setEyeColor(readEyeColor(defaultValue != null ? defaultValue.getEyeColor() : null));
            admin.setLocation(readLocation(defaultValue != null ? defaultValue.getLocation() : null));

            return admin;
        } catch (IllegalArgumentException e) {
            if (mode == CommandMode.CLI_UserMode) {
                System.out.println("Ошибка: " + e.getMessage());
                return readGroupAdmin(defaultValue); // Рекурсивный вызов
            } else {
                throw e;
            }
        }
    }

    public Color readEyeColor(Color defaultValue) {
        while (true) {
            try {
                if (mode == CommandMode.CLI_UserMode) {
                    String prompt = "Выберите цвет глаз";
                    if (defaultValue != null) prompt += " (пробел для " + defaultValue + "):";
                    else prompt += ":";
                    System.out.println(prompt);
                    for (Color color : Color.values()) {
                        System.out.println(color.ordinal() + 1 + " — " + color);
                    }
                    System.out.print("> ");
                }
                String input = scanner.nextLine().trim();
                if (defaultValue != null && input.isEmpty()) {
                    return defaultValue;
                }
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

    public Location readLocation(Location defaultValue) {
        while (true) {
            try {
                Float x, y, z;

                if (mode == CommandMode.CLI_UserMode) {
                    String xPrompt = "Введите координату X локации";
                    if (defaultValue != null) xPrompt += " (пробел для " + defaultValue.getX() + ")";
                    System.out.print(xPrompt + " > ");
                }
                String xInput = scanner.nextLine().trim();
                x = (defaultValue != null && xInput.isEmpty())
                        ? defaultValue.getX()
                        : Float.parseFloat(xInput);

                if (mode == CommandMode.CLI_UserMode) {
                    String yPrompt = "Введите координату Y локации";
                    if (defaultValue != null) yPrompt += " (пробел для " + defaultValue.getY() + ")";
                    System.out.print(yPrompt + " > ");
                }
                String yInput = scanner.nextLine().trim();
                y = (defaultValue != null && yInput.isEmpty())
                        ? defaultValue.getY()
                        : Float.parseFloat(yInput);

                if (mode == CommandMode.CLI_UserMode) {
                    String zPrompt = "Введите координату Z локации";
                    if (defaultValue != null) zPrompt += " (пробел для " + defaultValue.getZ() + ")";
                    System.out.print(zPrompt + " > ");
                }
                String zInput = scanner.nextLine().trim();
                z = (defaultValue != null && zInput.isEmpty())
                        ? defaultValue.getZ()
                        : Float.parseFloat(zInput);

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