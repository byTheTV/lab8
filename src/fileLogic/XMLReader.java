package fileLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Scanner;
import models.Color;
import models.Coordinates;
import models.FormOfEducation;
import models.Location;
import models.Person;
import models.StudyGroup;

public class XMLReader {

    /**
     * Десериализует коллекцию объектов StudyGroup из XML файла.
     *
     * @param filePath путь к XML файлу
     * @return коллекция объектов StudyGroup, восстановленных из файла
     */
    public static Collection<StudyGroup> readStudyGroupCollection(String filePath) {
        Collection<StudyGroup> groups = new ArrayDeque<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            StudyGroup currentGroup = null;
            Person currentPerson = null;
            boolean inGroupAdmin = false;
            
            // Чтение файла построчно с помощью Scanner
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                // Обработка начала и конца блока StudyGroup
                if (line.equals("<StudyGroup>")) {
                    currentGroup = new StudyGroup();
                    continue;
                }
                if (line.equals("</StudyGroup>")) {
                    if (currentGroup != null) {
                        groups.add(currentGroup);
                        currentGroup = null;
                    }
                    continue;
                }
                
                // Если работаем внутри блока StudyGroup
                if (currentGroup != null) {
                    // Обработка блока groupAdmin
                    if (line.equals("<groupAdmin>")) {
                        inGroupAdmin = true;
                        currentPerson = new Person();
                        continue;
                    }
                    if (line.equals("</groupAdmin>")) {
                        if (currentPerson != null) {
                            currentGroup.setGroupAdmin(currentPerson);
                        }
                        inGroupAdmin = false;
                        currentPerson = null;
                        continue;
                    }
                    
                    // Делегирование обработки строк в зависимости от контекста
                    if (inGroupAdmin) {
                        processAdminLine(line, currentPerson);
                    } else {
                        processGroupLine(line, currentGroup);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return groups;
    }
    
    /**
     * Обрабатывает строку с данными, относящимися к основным полям StudyGroup.
     *
     * @param line строка из файла
     * @param group объект StudyGroup, в который устанавливаются данные
     */
    private static void processGroupLine(String line, StudyGroup group) {
        String tag = getTagName(line);
        String value = getTagValue(line, tag);
        
        switch (tag) {
            case "id":
                try {
                    Integer id = Integer.valueOf(value);
                    java.lang.reflect.Field field = group.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(group, id);
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                break;
            case "name":
                group.setName(value);
                break;
            case "coordinates":
                try {
                    // Создаем Coordinates напрямую из значений
                    String[] parts = value.split(",");
                    Long x = Long.parseLong(parts[0].trim());

                    Long y = Long.parseLong(parts[1].trim());
                    group.setCoordinates(new Coordinates(x, y));
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге coordinates: " + value);
                    e.printStackTrace();
                }
                break;
            case "creationDate":
                try {
                    ZonedDateTime creationDate = ZonedDateTime.parse(value);
                    java.lang.reflect.Field field = group.getClass().getDeclaredField("creationDate");
                    field.setAccessible(true);
                    field.set(group, creationDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "studentsCount":
                group.setStudentsCount(Long.parseLong(value));
                break;
            case "expelledStudents":
                group.setExpelledStudents(Integer.parseInt(value));
                break;
            case "transferredStudents":
                group.setTransferredStudents(Integer.parseInt(value));
                break;
            case "formOfEducation":
                if (!value.isEmpty()) {
                    try {
                        FormOfEducation form = FormOfEducation.valueOf(value);
                        group.setFormOfEducation(form);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                // Неизвестный тег — игнорируем
                break;
        }
    }
    
    /**
     * Обрабатывает строку с данными, относящимися к полям объекта Person (groupAdmin).
     *
     * @param line строка из файла
     * @param person объект Person, в который устанавливаются данные
     */
    private static void processAdminLine(String line, Person person) {
        String tag = getTagName(line);
        String value = getTagValue(line, tag);
        
        switch (tag) {
            case "name":
                person.setName(value);
                break;
            case "birthday":
                try {
                    person.setBirthday(LocalDate.parse(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "passportID":
                person.setPassportID(value);
                break;
            case "eyeColor":
                try {
                    person.setEyeColor(Color.valueOf(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "location":
                try {
                    // Создаем Location напрямую из значений
                    String[] parts = value.split(",");
                    Float x = Float.parseFloat(parts[0].trim());
                    Float y = Float.parseFloat(parts[1].trim());
                    Float z = Float.parseFloat(parts[2].trim());
                    person.setLocation(new Location(x, y, z));
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге location: " + value);
                    e.printStackTrace();
                }
                break;
            default:
                // Неизвестный тег — игнорируем
                break;
        }
    }
    
    /**
     * Извлекает имя тега из строки.
     *
     * @param line строка, содержащая XML-тег
     * @return имя тега без символов '<' и '>'
     */
    private static String getTagName(String line) {
        int start = line.indexOf("<");
        int end = line.indexOf(">");
        if (start != -1 && end != -1 && end > start) {
            return line.substring(start + 1, end).trim();
        }
        return "";
    }
    
    /**
     * Извлекает значение между открывающим и закрывающим тегом.
     * Теперь поддерживает многострочные значения.
     */
    private static String getTagValue(String line, String tagName) {
        String openTag = "<" + tagName + ">";
        String closeTag = "</" + tagName + ">";
        int start = line.indexOf(openTag);
        int end = line.indexOf(closeTag);
        if (start != -1 && end != -1) {
            return line.substring(start + openTag.length(), end)
                      .trim()
                      .replaceAll("\n\\s*", ""); // Удаляем переносы строк и лишние пробелы
        }
        return "";
    }
} 