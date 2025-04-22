package Server.collectionManagers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static Map<String, Integer> groupCountingByFormOfEducation(ArrayDeque<StudyGroup> collection) {
        return collection.stream()
                .collect(Collectors.groupingBy(
                        group -> group.getFormOfEducation().toString(),
                        Collectors.summingInt(e -> 1)
                ));
    }

    /**
     * Gets command descriptions for help command
     * @return Map with command names and their descriptions
     */
    public static Map<String, String> getCommandDescriptions() {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("help", "Show available commands");
        descriptions.put("info", "Show collection information");
        descriptions.put("show", "Show all elements");
        descriptions.put("add", "Add new element");
        descriptions.put("update_id", "Update element by ID");
        descriptions.put("remove_by_id", "Remove element by ID");
        descriptions.put("clear", "Clear collection");
        descriptions.put("head", "Show first element");
        descriptions.put("remove_head", "Remove and return first element");
        descriptions.put("remove_lower", "Remove elements lower than given");
        descriptions.put("average_of_transferred_students", "Calculate average of transferred students");
        descriptions.put("group_counting_by_form_of_education", "Group elements by form of education");
        descriptions.put("print_field_ascending_group_admin", "Print group admin names in ascending order");
        return descriptions;
    }

    /**
     * Gets list of group admin names in ascending order
     * @param collection the collection to process
     * @return List of admin names
     */
    public static List<String> printFieldAscendingGroupAdmin(ArrayDeque<StudyGroup> collection) {
        return collection.stream()
                .map(group -> group.getGroupAdmin().getName())
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Shows all elements in the collection
     * @param collection the collection to show
     * @return List of study groups
     */
    public static List<StudyGroup> show(ArrayDeque<StudyGroup> collection) {
        return new ArrayList<>(collection);
    }

    /**
     * Calculates average of transferred students
     * @param collection the collection to process
     * @return average value
     */
    public static double averageOfTransferredStudents(ArrayDeque<StudyGroup> collection) {
        if (collection.isEmpty()) {
            throw new IllegalStateException("Collection is empty");
        }
        return collection.stream()
                .mapToInt(StudyGroup::getTransferredStudents)
                .average()
                .orElse(0.0);
    }
} 