package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;

public class Show extends Command {
    public Show(StudyGroupCollectionManager collectionManager) {
        super(false, collectionManager);
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescr() {
        return "вывести все элементы коллекции";
    }

    @Override
    public void execute() {
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        collectionManager.getCollection().forEach(System.out::println);
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 