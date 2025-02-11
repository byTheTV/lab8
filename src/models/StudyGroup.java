package models;

import java.time.ZonedDateTime;
import java.util.Objects;

public class StudyGroup implements Comparable<StudyGroup> {
    private Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private ZonedDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long studentsCount; // Значение поля должно быть больше 0
    private int expelledStudents; // Значение поля должно быть больше 0
    private int transferredStudents; // Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; // Поле может быть null
    private Person groupAdmin; // Поле не может быть null

    public StudyGroup() {
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
}