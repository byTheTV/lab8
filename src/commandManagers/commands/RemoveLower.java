package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;
import models.StudyGroup;
import java.util.Iterator;

/**
 * Команда remove_lower: удаляет из коллекции все элементы, меньшие, чем заданный.
 * Ожидается, что аргумент команды является объектом StudyGroup.
 */
public class RemoveLower extends Command {

    public RemoveLower(StudyGroupCollectionManager collectionManager) {
        // Аргумент обязателен, поэтому передаём true в суперконструктор.
        super(true, collectionManager);
    }
    
    @Override
    public String getName() {
        return "remove_lower";
    }
    
    @Override
    public String getDescr() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }
    
    @Override
    public void execute() {
        // Предполагается, что базовый класс Command предоставляет метод getArgument()
        Object arg = getArgument();
        if (!(arg instanceof StudyGroup)) {
            System.out.println("Неверный аргумент для команды remove_lower. Ожидался объект StudyGroup.");
            return;
        }
        
        StudyGroup given = (StudyGroup) arg;
        int removedCount = 0;
        Iterator<StudyGroup> iterator = collectionManager.getCollection().iterator();
        while (iterator.hasNext()) {
            StudyGroup sg = iterator.next();
            if (sg.compareTo(given) < 0) {
                iterator.remove();
                removedCount++;
            }
        }
        
        System.out.println("Удалено элементов: " + removedCount);
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof StudyGroup;
    }
} 