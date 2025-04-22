package Client.commandManagers.commands;

import Client.commandManagers.Command;

/**
 * Команда remove_head: выводит первый элемент коллекции и удаляет его.
 */
public class RemoveHead extends Command {

    public RemoveHead() {
        super(false);
    }
    
    @Override
    public String getName() {
        return "remove_head";
    }
    
    @Override
    public String getDescr() {
        return "вывести первый элемент коллекции и удалить его";
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
            iterator.remove();
            System.out.println("Элемент удалён.");
        }

         */
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 