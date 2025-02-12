package commandManagers.commands;

import collectionManagers.StudyGroupCollectionManager;
import commandManagers.Command;

public class SaveCollection extends Command {
    private final StudyGroupCollectionManager collectionManager;
    
    public SaveCollection(StudyGroupCollectionManager collectionManager) {
        super(false, collectionManager);
        this.collectionManager = collectionManager;
    }
    
    @Override
    public String getName() {
        return "save";
    }

    @Override
    public String getDescr() {
        return "сохранить коллекцию в файл";
    }

    @Override
    public void execute() {
        if (collectionManager.saveCollection()) {
            System.out.println("Коллекция успешно сохранена");
        } else {
            System.err.println("Ошибка при сохранении коллекции");
        }
    }
    
    @Override
    public void setArgument(String arg) {
        // This command does not require any argument.
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
    
} 