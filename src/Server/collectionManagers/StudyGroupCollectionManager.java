package Server.collectionManagers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import Common.models.StudyGroup;
import Server.database.DatabaseManager;
import Server.database.StudyGroupDAO;

/**
 * Class that manages the collection of StudyGroup objects.
 * Provides methods for adding, removing, and manipulating the collection.
 */
public class StudyGroupCollectionManager {
    private final StudyGroupDAO dao;
    private final LocalDateTime creationDate;
    private final String collectionType;

    public StudyGroupCollectionManager() {
        try {
            this.dao = new StudyGroupDAO(DatabaseManager.getConnection());
            this.creationDate = LocalDateTime.now();
            this.collectionType = "DatabaseCollection";
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    /**
     * Adds a study group to the collection
     * @param studyGroup the study group to add
     */
    public void add(StudyGroup studyGroup) {
        try {
            dao.add(studyGroup, 1); // TODO: Pass actual user ID
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add study group: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add study group", e);
        }
    }

    /**
     * Removes a study group by its ID
     * @param id the ID of the study group to remove
     * @return true if removed successfully, false if not found
     */
    public boolean removeById(long id) {
        try {
            return dao.removeById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove study group", e);
        }
    }

    /**
     * Updates a study group with the given ID
     * @param id the ID of the study group to update
     * @param newStudyGroup the new study group data
     * @return true if updated successfully, false if not found
     */
    public boolean updateById(long id, StudyGroup newStudyGroup) {
        try {
            return dao.updateById(id, newStudyGroup);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update study group", e);
        }
    }

    /**
     * Clears the collection
     */
    public void clear() {
        try {
            dao.clear();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear collection", e);
        }
    }

    /**
     * Returns the first element of the collection
     * @return the first StudyGroup or null if collection is empty
     */
    public StudyGroup getHead() {
        try {
            List<StudyGroup> groups = dao.getAll();
            return groups.isEmpty() ? null : groups.get(0);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get head element", e);
        }
    }

    /**
     * Removes and returns the first element
     * @return the removed StudyGroup or null if collection is empty
     */
    public StudyGroup removeHead() {
        try {
            List<StudyGroup> groups = dao.getAll();
            if (groups.isEmpty()) {
                return null;
            }
            StudyGroup head = groups.get(0);
            dao.removeById(head.getId());
            return head;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove head element", e);
        }
    }

    /**
     * Removes all elements lower than the given study group
     * @param studyGroup the study group to compare with
     */
    public int removeLower(StudyGroup studyGroup) {
        try {
            List<StudyGroup> groups = dao.getAll();
            int initialSize = groups.size();
            for (StudyGroup group : groups) {
                if (group.compareTo(studyGroup) < 0) {
                    dao.removeById(group.getId());
                }
            }
            return initialSize - dao.getAll().size();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove lower elements", e);
        }
    }

    /**
     * Calculates average of transferred students across all groups
     * @return average number of transferred students
     */
    public double getAverageOfTransferredStudents() {
        try {
            List<StudyGroup> groups = dao.getAll();
            if (groups.isEmpty()) return 0;
            return groups.stream()
                    .mapToInt(group -> (int)group.getTransferredStudents())
                    .average()
                    .orElse(0);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to calculate average", e);
        }
    }

    /**
     * Gets the collection
     * @return the collection of study groups
     */
    public List<StudyGroup> getCollection() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get collection", e);
        }
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
        try {
            return dao.getAll().size();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get collection size", e);
        }
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
        try {
            List<StudyGroup> groups = dao.getAll();
            for (StudyGroup group : groups) {
                sb.append(group).append("\n");
            }
        } catch (SQLException e) {
            sb.append("Ошибка при получении элементов коллекции: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public StudyGroup getById(Long id) {
        try {
            return dao.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get study group by ID", e);
        }
    }

    public Map<String, Integer> groupCountingByFormOfEducation() {
        try {
            List<StudyGroup> groups = dao.getAll();
            return StudyGroupCollectionUtils.groupCountingByFormOfEducation(groups);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count groups by form of education", e);
        }
    }

    public Map<String, String> getCommandDescriptions() {
        return StudyGroupCollectionUtils.getCommandDescriptions();
    }

    public List<String> printFieldAscendingGroupAdmin() {
        try {
            List<StudyGroup> groups = dao.getAll();
            return StudyGroupCollectionUtils.printFieldAscendingGroupAdmin(groups);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to print field ascending group admin", e);
        }
    }

    public List<StudyGroup> show() {
        try {
            return dao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to show collection", e);
        }
    }

    public double averageOfTransferredStudents() {
        try {
            List<StudyGroup> groups = dao.getAll();
            return StudyGroupCollectionUtils.averageOfTransferredStudents(groups);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to calculate average of transferred students", e);
        }
    }
} 