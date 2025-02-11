import java.time.ZonedDateTime;

public class StudyGroup implements Comparable<StudyGroup> {
    private static Integer nextId = 1; // Для автоматической генерации ID

    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long studentsCount; //Значение поля должно быть больше 0
    private int expelledStudents; //Значение поля должно быть больше 0
    private int transferredStudents; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле может быть null
    private Person groupAdmin; //Поле не может быть null

    
   public StudyGroup(String name, Coordinates coordinates, long studentsCount,
                      int expelledStudents, int transferredStudents,
                      FormOfEducation formOfEducation, Person groupAdmin) {
        setId(nextId++);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate();
        setStudentsCount(studentsCount);
        setExpelledStudents(expelledStudents);
        setTransferredStudents(transferredStudents);
        setFormOfEducation(formOfEducation);
        setGroupAdmin(groupAdmin);
    }

    public static void updateNextId(java.util.Collection<StudyGroup> groups) {
        nextId = groups.stream()
                .mapToInt(group -> group.id)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public int compareTo(StudyGroup other) {
        if (other == null) return 1;
        return this.id.compareTo(other.id);
    }

    private void setCreationDate() {
        this.creationDate = ZonedDateTime.now();
    }

    public void setId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID не может быть null и должен быть больше 0");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name;
    }

    public void setStudentsCount(long studentsCount) {
        if (studentsCount <= 0) {
            throw new IllegalArgumentException("Количество студентов должно быть больше 0");
        }
        this.studentsCount = studentsCount;
    }

    public void setExpelledStudents(int expelledStudents) {
        if (expelledStudents <= 0) {
            throw new IllegalArgumentException("Количество отчисленных студентов должно быть больше 0");
        }
        this.expelledStudents = expelledStudents;
    }

    public void setTransferredStudents(int transferredStudents) {
        if (transferredStudents <= 0) {
            throw new IllegalArgumentException("Количество переведенных студентов должно быть больше 0");
        }
        this.transferredStudents = transferredStudents;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public void setGroupAdmin(Person groupAdmin) {
        if (groupAdmin == null) {
            throw new IllegalArgumentException("Админ группы не может быть null");
        }
        this.groupAdmin = groupAdmin;
    }

    public Integer getId() {
        return this.id;
    }
}