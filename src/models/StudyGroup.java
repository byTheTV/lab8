package models;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Класс StudyGroup представляет учебную группу с набором характеристик, включая объект-администратора.
 * Поле id генерируется автоматически, его значение > 0 и уникально.
 */
public class StudyGroup implements Comparable<StudyGroup> {
    private static int idCounter = 1; // Счётчик для автоматической генерации уникального id

    private Integer id; // Поле не может быть null, значение > 0, уникальное, генерируется автоматически
    private String name; // Поле не может быть null, не пустое
    private Coordinates coordinates; // Поле не может быть null
    private ZonedDateTime creationDate; // Поле не может быть null, генерируется автоматически
    private long studentsCount; // Значение > 0
    private int expelledStudents; // Значение > 0
    private int transferredStudents; // Значение > 0
    private FormOfEducation formOfEducation; // Может быть null
    private Person groupAdmin; // Поле не может быть null

    public StudyGroup() {
        this.id = idCounter++;                // Автоматическая генерация уникального id
        this.creationDate = ZonedDateTime.now();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.coordinates = coordinates;
    }

    public void setStudentsCount(long studentsCount) {
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Students count must be positive");
        }
        this.studentsCount = studentsCount;
    }

    public void setExpelledStudents(int expelledStudents) {
        if (expelledStudents <= 0) {
            throw new IllegalArgumentException("Expelled students count must be positive");
        }
        this.expelledStudents = expelledStudents;
    }

    public void setTransferredStudents(int transferredStudents) {
        if (transferredStudents <= 0) {
            throw new IllegalArgumentException("Transferred students count must be positive");
        }
        this.transferredStudents = transferredStudents;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public void setGroupAdmin(Person groupAdmin) {
        if (groupAdmin == null) {
            throw new IllegalArgumentException("Group admin cannot be null");
        }
        this.groupAdmin = groupAdmin;
    }

    // Геттеры
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public ZonedDateTime getCreationDate() { return creationDate; }
    public long getStudentsCount() { return studentsCount; }
    public int getExpelledStudents() { return expelledStudents; }
    public int getTransferredStudents() { return transferredStudents; }
    public FormOfEducation getFormOfEducation() { return formOfEducation; }
    public Person getGroupAdmin() { return groupAdmin; }

    @Override
    public int compareTo(StudyGroup other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyGroup that = (StudyGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Переопределенный метод toString() возвращает красиво отформатированное представление объекта StudyGroup.
     * Информация об администраторе выводится полностью с отступом для улучшенной читаемости.
     */
    @Override
    public String toString() {
        String separator = "----------------------------------------\n";
        StringBuilder sb = new StringBuilder();
        
        sb.append(separator);
        sb.append("StudyGroup Details:\n");
        sb.append(separator);
        sb.append(String.format("ID                 : %s%n", id != null ? id : "N/A"));
        sb.append(String.format("Name               : %s%n", name != null ? name : "N/A"));
        sb.append(String.format("Coordinates        : %s%n", coordinates != null ? coordinates.toString() : "N/A"));
        sb.append(String.format("Creation Date      : %s%n", creationDate != null ? creationDate.toString() : "N/A"));
        sb.append(String.format("Students Count     : %d%n", studentsCount));
        sb.append(String.format("Expelled Students  : %d%n", expelledStudents));
        sb.append(String.format("Transferred Students: %d%n", transferredStudents));
        sb.append(String.format("Form Of Education  : %s%n", formOfEducation != null ? formOfEducation : "N/A"));
        sb.append("Group Admin:\n");
        if (groupAdmin != null) {
            // Вызывается красивый toString() объекта Person с отступами
            String adminDetails = groupAdmin.toString().replaceAll("(?m)^", "    ");
            sb.append(adminDetails);
        } else {
            sb.append("    None\n");
        }
        sb.append(separator);
        return sb.toString();
    }
}