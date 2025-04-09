package client.commandManagers.commands;

import client.commandManagers.Command;
import client.models.StudyGroup;
import java.util.Iterator;

/**
 * Команда head: выводит первый элемент коллекции.
 */
public class Head extends Command {

    public Head() {
        super(false);
    }
    
    @Override
    public String getName() {
        return "head";
    }
    
    @Override
    public String getDescr() {
        return "вывести первый элемент коллекции";
    }
    
    @Override
    public void execute() {
       /*
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        Iterator<StudyGroup> iterator = collectionManager.getCollection().iterator();
        if (iterator.hasNext()) {
            StudyGroup first = iterator.next();
            System.out.println(first);
        }

        */
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 