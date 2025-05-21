package Server.collectionManagers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Common.models.FormOfEducation;
import Common.models.StudyGroup;

/**
 * Utility class containing additional methods for StudyGroup collection management
 */
public class StudyGroupCollectionUtils {

    /**
     * Groups and counts study groups by form of education
     * @param collection the collection to process
     * @return Map with form of education as key and count as value
     */
    public static Map<String, Integer> groupCountingByFormOfEducation(List<StudyGroup> collection) {
        Map<String, Integer> counts = new HashMap<>();
        for (FormOfEducation form : FormOfEducation.values()) {
            counts.put(form.toString(), 0);
        }
        
        for (StudyGroup group : collection) {
            String form = group.getFormOfEducation().toString();
            counts.put(form, counts.get(form) + 1);
        }
        
        return counts;
    }

    /**
     * Gets command descriptions for help command
     * @return Map with command names and their descriptions
     */
    public static Map<String, String> getCommandDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("add", "Добавить новый элемент в коллекцию");
        descriptions.put("clear", "Очистить коллекцию");
        descriptions.put("group_counting_by_form_of_education", "Сгруппировать элементы коллекции по значению поля formOfEducation");
        descriptions.put("head", "Вывести первый элемент коллекции");
        descriptions.put("help", "Вывести справку по доступным командам");
        descriptions.put("info", "Вывести информацию о коллекции");
        descriptions.put("print_field_ascending_group_admin", "Вывести значения поля groupAdmin всех элементов в порядке возрастания");
        descriptions.put("remove_by_id", "Удалить элемент из коллекции по его id");
        descriptions.put("remove_head", "Вывести и удалить первый элемент коллекции");
        descriptions.put("remove_lower", "Удалить из коллекции все элементы, меньшие, чем заданный");
        descriptions.put("show", "Вывести все элементы коллекции");
        descriptions.put("update_id", "Обновить значение элемента коллекции, id которого равен заданному");
        descriptions.put("average_of_transferred_students", "Вывести среднее значение поля transferredStudents для всех элементов коллекции");
        return descriptions;
    }

    /**
     * Gets list of group admin names in ascending order
     * @param collection the collection to process
     * @return List of admin names
     */
    public static List<String> printFieldAscendingGroupAdmin(List<StudyGroup> collection) {
        return collection.stream()
                .map(group -> group.getGroupAdmin().getName())
                .sorted()
                .toList();
    }

    /**
     * Shows all elements in the collection
     * @param collection the collection to show
     * @return List of study groups
     */
    public static List<StudyGroup> show(List<StudyGroup> collection) {
        return new ArrayList<>(collection);
    }

    /**
     * Calculates average of transferred students
     * @param collection the collection to process
     * @return average value
     */
    public static double averageOfTransferredStudents(List<StudyGroup> collection) {
        if (collection.isEmpty()) return 0;
        return collection.stream()
                .mapToInt(group -> (int)group.getTransferredStudents())
                .average()
                .orElse(0);
    }
} 