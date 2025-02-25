package models;

import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;

/**
 * Класс {@code Person} представляет администратора группы с информацией о личности.
 */
public class Person implements Comparable<Person> {
   
    private String name; // Поле не может быть null, Строка не может быть пустой
    private LocalDate birthday; // Поле может быть null
    private String passportID; // Значение этого поля должно быть уникальным, Длина строки не должна быть больше 26, Строка не может быть пустой, Поле может быть null
    private Color eyeColor; // Поле не может быть null
    private Location location; // Поле не может быть null

   // private static Set<String> usedPassportIDs = new HashSet<>();

    /**
     * Конструктор по умолчанию.
     */
    public Person() {}

    /**
     * Конструктор с параметрами для задания основных характеристик.
     *
     * @param name имя администратора.
     * @param birthday дата рождения администратора.
     * @param passportID идентификатор паспорта.
     */
    public Person(String name, LocalDate birthday, String passportID, 
                 Color eyeColor, Location location) {
        setName(name);
        this.birthday = birthday;
        setPassportID(passportID);
        setEyeColor(eyeColor);
        setLocation(location);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setPassportID(String passportID) {
        if (passportID != null) {
            if (passportID.trim().isEmpty()) {
                throw new IllegalArgumentException("Passport ID cannot be empty if provided");
            }
            if (passportID.length() > 26) {
                throw new IllegalArgumentException("Passport ID length cannot exceed 26 characters");
            }
        }
        this.passportID = passportID;
    }

    public void setEyeColor(Color eyeColor) {
        if (eyeColor == null) {
            throw new IllegalArgumentException("Eye color cannot be null");
        }
        this.eyeColor = eyeColor;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        this.location = location;
    }

    /**
     * Возвращает имя администратора.
     *
     * @return имя.
     */
    public String getName() { return name; }
    public LocalDate getBirthday() { return birthday; }
    public String getPassportID() { return passportID; }
    public Color getEyeColor() { return eyeColor; }
    public Location getLocation() { return location; }

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Возвращает строковое представление объекта {@code Person} с подробностями.
     *
     * @return отформатированная строка с характеристиками администратора.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String separator = "====================================\n";
        sb.append(separator)
          .append("            Person Details          \n")
          .append(separator)
          .append(String.format("Name         : %s%n", name != null ? name : "N/A"))
          .append(String.format("Birthday     : %s%n", birthday != null ? birthday.toString() : "N/A"))
          .append(String.format("Passport ID  : %s%n", passportID != null ? passportID : "N/A"))
          .append(String.format("Eye Color    : %s%n", eyeColor != null ? eyeColor.toString() : "N/A"))
          .append(String.format("Location     : %s%n", location != null ? location.toString() : "N/A"))
          .append(separator);
        return sb.toString();
    }
}
