package Common.models;

import java.time.LocalDate;

public class StudyGroupExample {
    public StudyGroup StudyGroupExample() {
        // Создаем координаты
        Coordinates coordinates = new Coordinates(100L, 200L);
        
        // Создаем местоположение
        Location location = new Location(10.5f, 20.5f, 30.5f);
        
        // Создаем администратора группы
        Person groupAdmin = new Person(
            "Иван Иванов",
            LocalDate.of(1995, 5, 15),
            "AB123456",
            Color.GREEN,
            location
        );
        
        // Создаем учебную группу
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setName("Группа 101");
        studyGroup.setCoordinates(coordinates);
        studyGroup.setStudentsCount(30);
        studyGroup.setExpelledStudents(2);
        studyGroup.setTransferredStudents(1);
        studyGroup.setFormOfEducation(FormOfEducation.FULL_TIME_EDUCATION);
        studyGroup.setGroupAdmin(groupAdmin);
        
        return studyGroup;

    } 
}