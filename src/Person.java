import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Person implements Comparable<Person> {
   
    private String name; //Поле не может быть null, Строка не может быть пустой
    private LocalDate birthday; //Поле может быть null
    private String passportID; //Значение этого поля должно быть уникальным, Длина строки не должна быть больше 26, Строка не может быть пустой, Поле может быть null
    private Color eyeColor; //Поле не может быть null
    private Location location; //Поле не может быть null

    private static Set<String> usedPassportIDs = new HashSet<>();

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
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        this.name = name;
    }

    public void setPassportID(String passportID) {
        if (passportID == null) {
            this.passportID = null;
            return;
        }

        if (passportID.trim().isEmpty()) {
            throw new IllegalArgumentException("Номер паспорта не может быть пустым");
        }

        if (passportID.length() > 26) {
            throw new IllegalArgumentException("Длина номера паспорта не может быть больше 26 символов");
        }

        if (usedPassportIDs.contains(passportID)) {
            throw new IllegalArgumentException("Этот номер паспорта уже используется");
        }

        this.passportID = passportID;
        usedPassportIDs.add(passportID);
    }

    public void setEyeColor(Color eyeColor) {
        if (eyeColor == null) {
            throw new IllegalArgumentException("Цвет глаз не может быть null");
        }
        this.eyeColor = eyeColor;
    }

    public void setLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Локация не может быть null");
        }
        this.location = location;
    }

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }

    // Геттеры
}
