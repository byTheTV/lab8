package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;

public class Info extends Command {
    public Info(StudyGroupCollectionManager collectionManager) {
        super(false, collectionManager);
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescr() {
        return "вывести информацию о коллекции";
    }

    @Override
    public void execute() {
        System.out.println("Тип коллекции: " + collectionManager.getCollectionType());
        System.out.println("Дата инициализации: " + collectionManager.getCreationDate());
        System.out.println("Количество элементов: " + collectionManager.getSize());
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 