package collectionManagers;

import models.StudyGroup;
import java.time.LocalDateTime;
import java.util.ArrayDeque;

/**
 * Class that manages the collection of StudyGroup objects.
 * Provides methods for adding, removing, and manipulating the collection.
 */
public class StudyGroupCollectionManager {
    private ArrayDeque<StudyGroup> collection;
    private LocalDateTime creationDate;
    private String collectionType;

    /**
     * Constructor initializes an empty collection
     */
    public StudyGroupCollectionManager() {
        this.collection = new ArrayDeque<>();
        this.creationDate = LocalDateTime.now();
        this.collectionType = collection.getClass().getSimpleName();
    }

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
    public void removeLower(StudyGroup studyGroup) {
        collection.removeIf(group -> group.compareTo(studyGroup) < 0);
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
} 