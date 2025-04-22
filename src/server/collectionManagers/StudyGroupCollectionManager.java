package Server.collectionManagers;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;

import Common.models.StudyGroup;

/**
 * Class that manages the collection of StudyGroup objects.
 * Provides methods for adding, removing, and manipulating the collection.
 */
public class StudyGroupCollectionManager {
    private final ArrayDeque<StudyGroup> collection;
    private final LocalDateTime creationDate;
    private final String collectionType;
    private String dataFile;

    public StudyGroupCollectionManager() {
        this.creationDate = LocalDateTime.now();
        this.collection = new ArrayDeque<>();
        this.collectionType = collection.getClass().getSimpleName();
    }

    /**
     * Инициализирует путь к файлу с данными.
     *
     * @param filename имя файла с данными
     * @return true, если инициализация прошла успешно, иначе false
     */
    public boolean initializeData(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println("Имя файла не может быть пустым!");
            return false;
        }
        this.dataFile = filename;
        return true;
    }

    /*
    public boolean load() {
        try {
            Collection<StudyGroup> loadedGroups = XMLReader.readStudyGroupCollection(dataFile);
            collection.clear();
            collection.addAll(loadedGroups);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
     //       e.printStackTrace();
            return false;
        }
    }
    

    
    public boolean saveCollection() {
        try {
            XMLWriter.writeStudyGroupCollection(collection, dataFile);
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
            return false;
        }
    }
    */


    /**
     * Adds a study group to the collection
     * @param studyGroup the study group to add
     */
    public void add(StudyGroup studyGroup) {
        collection.add(studyGroup);
    }

    /**
     * Removes a study group by its ID
     * @param id the ID of the study group to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeById(long id) {
        return collection.removeIf(group -> group.getId() == id);
    }

    /**
     * Updates a study group with the given ID
     * @param id the ID of the study group to update
     * @param newStudyGroup the new study group data
     * @return true if updated successfully, false if not found
     */
    public boolean updateById(long id, StudyGroup newStudyGroup) {
        for (StudyGroup group : collection) {
            if (group.getId() == id) {
                collection.remove(group);
                collection.add(newStudyGroup);
                return true;
            }
        }
        return false;
    }

    /**
     * Clears the collection
     */
    public void clear() {
        collection.clear();
    }


    /**
     * Returns the first element of the collection
     * @return the first StudyGroup or null if collection is empty
     */
    public StudyGroup getHead() {
        return collection.isEmpty() ? null : collection.getFirst();
    }

    /**
     * Removes and returns the first element
     * @return the removed StudyGroup or null if collection is empty
     */
    public StudyGroup removeHead() {
        return collection.isEmpty() ? null : collection.removeFirst();
    }

    /**
     * Removes all elements lower than the given study group
     * @param studyGroup the study group to compare with
     */
    public int removeLower(StudyGroup studyGroup) {
        int initialSize = collection.size();
        collection.removeIf(group -> group.compareTo(studyGroup) < 0);
        return initialSize - collection.size();
    }
    /**
     * Calculates average of transferred students across all groups
     * @return average number of transferred students
     */
    public double getAverageOfTransferredStudents() {
        if (collection.isEmpty()) return 0;
        return collection.stream()
                .mapToInt(StudyGroup::getTransferredStudents)
                .average()
                .orElse(0);
    }

    /**
     * Gets the collection
     * @return the collection of study groups
     */
    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
    }

    /**
     * Gets creation date of the collection
     * @return creation date
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Gets collection type
     * @return collection type as string
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * Gets collection size
     * @return number of elements in collection
     */
    public int getSize() {
        return collection.size();
    }


    /**
     * Возвращает строковое представление состояния коллекции.
     *
     * @return строка с информацией о коллекции
     */
    public String to_string() {
        StringBuilder sb = new StringBuilder();
        sb.append("Тип коллекции: ").append(collectionType).append("\n")
          .append("Размер коллекции: ").append(getSize()).append("\n")
          .append("Дата создания: ").append(creationDate).append("\n")
          .append("Элементы коллекции:\n");
        for (StudyGroup group : collection) {
            sb.append(group).append("\n");
        }
        return sb.toString();
    }

    public StudyGroup getById(Long id) {
        return collection.stream()
                .filter(group -> group.getId().equals(id.intValue()))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Integer> groupCountingByFormOfEducation() {
        return StudyGroupCollectionUtils.groupCountingByFormOfEducation(collection);
    }

    public Map<String, String> getCommandDescriptions() {
        return StudyGroupCollectionUtils.getCommandDescriptions();
    }

    public List<String> printFieldAscendingGroupAdmin() {
        return StudyGroupCollectionUtils.printFieldAscendingGroupAdmin(collection);
    }

    public List<StudyGroup> show() {
        return StudyGroupCollectionUtils.show(collection);
    }

    public double averageOfTransferredStudents() {
        return StudyGroupCollectionUtils.averageOfTransferredStudents(collection);
    }

    /**
     * Executes a single command
     * @param command the command to execute
     * @param argument the command argument (if any)
     * @return result of command execution
     */
    public String executeCommand(String command, String argument) {
        try {
            switch (command) {
                case "add":
                    // Здесь нужно реализовать создание StudyGroup из аргументов
                    return "Команда add выполнена";
                case "clear":
                    clear();
                    return "Коллекция очищена";
                case "show":
                    return to_string();
                case "info":
                    return String.format("Тип коллекции: %s\nРазмер: %d\nДата создания: %s",
                            collectionType, getSize(), creationDate);
                case "help":
                    return "Доступные команды: add, clear, show, info, help";
                case "remove_by_id":
                    if (argument == null) {
                        return "Ошибка: не указан ID для удаления";
                    }
                    try {
                        long id = Long.parseLong(argument);
                        if (removeById(id)) {
                            return "Элемент с ID " + id + " успешно удален";
                        } else {
                            return "Элемент с ID " + id + " не найден";
                        }
                    } catch (NumberFormatException e) {
                        return "Ошибка: ID должен быть числом";
                    }
                case "remove_head":
                    StudyGroup head = removeHead();
                    if (head != null) {
                        return "Первый элемент коллекции удален:\n" + head;
                    } else {
                        return "Коллекция пуста";
                    }
                case "remove_lower":
                    if (argument == null) {
                        return "Ошибка: не указан ID для сравнения";
                    }
                    try {
                        long id = Long.parseLong(argument);
                        StudyGroup targetGroup = getById(id);
                        if (targetGroup == null) {
                            return "Элемент с ID " + id + " не найден";
                        }
                        int count = removeLower(targetGroup);
                        return "Удалено элементов: " + count;
                    } catch (NumberFormatException e) {
                        return "Ошибка: ID должен быть числом";
                    }
                case "average_of_transferred_students":
                    double average = averageOfTransferredStudents();
                    return "Среднее количество переведенных студентов: " + average;
                case "group_counting_by_form_of_education":
                    Map<String, Integer> counts = groupCountingByFormOfEducation();
                    StringBuilder sb = new StringBuilder("Количество групп по формам обучения:\n");
                    counts.forEach((form, count) -> sb.append(form).append(": ").append(count).append("\n"));
                    return sb.toString();
                case "print_field_ascending_group_admin":
                    List<String> adminNames = printFieldAscendingGroupAdmin();
                    StringBuilder adminSb = new StringBuilder("Имена администраторов в порядке возрастания:\n");
                    adminNames.forEach(name -> adminSb.append(name).append("\n"));
                    return adminSb.toString();
                default:
                    return "Неизвестная команда: " + command;
            }
        } catch (Exception e) {
            return "Ошибка при выполнении команды " + command + ": " + e.getMessage();
        }
    }
} 