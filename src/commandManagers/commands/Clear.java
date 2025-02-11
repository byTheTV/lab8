package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;

public class Clear extends Command {
    public Clear(StudyGroupCollectionManager collectionManager) {
        super(false, collectionManager);
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescr() {
        return "очистить коллекцию";
    }

    @Override
    public void execute() {
        collectionManager.clear();
        System.out.println("Коллекция очищена");
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 