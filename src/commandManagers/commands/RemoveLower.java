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
        Long id = null;

        if (argument == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        try {
            id = Long.parseLong((String) argument);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Argument is not a valid long value", e);
        }

        StudyGroup given = collectionManager.getById(id);
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