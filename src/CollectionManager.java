import java.time.ZonedDateTime;
import java.util.ArrayDeque;

public class CollectionManager {
    private ArrayDeque<StudyGroup> collection;
    private final ZonedDateTime initializationDate;
 //   private String fileName;

    public CollectionManager(/*String fileName*/) {
        this.collection = new ArrayDeque<>();
        this.initializationDate = ZonedDateTime.now();
 //       this.fileName = fileName;
    }

    public void add(StudyGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("Группа не может быть null");
        }
        collection.add(group);
    }
    /*
    public void update(int id, StudyGroup updatedGroup) {
        if (updatedGroup == null) {
            throw new IllegalArgumentException("Обновленная группа не может быть null");
        }
        
        boolean found = false;
        ArrayDeque<StudyGroup> tempDeque = new ArrayDeque<>();
        
        while (!collection.isEmpty()) {
            StudyGroup current = collection.remove();
            if (current.getId() == id) {
                updatedGroup.setId(id);
                tempDeque.add(updatedGroup);
                found = true;
            } else {
                tempDeque.add(current);
            }
        }
        
        collection = tempDeque;
        
        if (!found) {
            throw new IllegalArgumentException("Группа с ID " + id + " не найдена");
        }
    }
    */
   
    public void removeById(int id) {
        boolean found = false;
        ArrayDeque<StudyGroup> tempDeque = new ArrayDeque<>();
        
        while (!collection.isEmpty()) {
            StudyGroup current = collection.remove();
            if (current.getId() != id) {
                tempDeque.add(current);
            } else {
                found = true;
            }
        }
        
        collection = tempDeque;
        
        if (!found) {
            throw new IllegalArgumentException("Группа с ID " + id + " не найдена");
        }
    }

    public void clear() {
        collection.clear();
    }

    public void show() {
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        
        for (StudyGroup group : collection) {
            System.out.println(group);
        }
    }

    public void printInfo() {
        System.out.println("Тип: ArrayDeque<StudyGroup>");
        System.out.println("Дата инициализации: " + initializationDate);
        System.out.println("Количество элементов: " + collection.size());
    }

    // Геттеры для доступа к коллекции и дате инициализации
    public ArrayDeque<StudyGroup> getCollection() {
        return collection;
    }

    public ZonedDateTime getInitializationDate() {
        return initializationDate;
    }
}