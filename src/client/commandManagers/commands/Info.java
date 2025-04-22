package Client.commandManagers.commands;

import Client.commandManagers.Command;

public class Info extends Command {
    public Info() {
        super(false);
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
        /*
        System.out.println("Тип коллекции: " + collectionManager.getCollectionType());
        System.out.println("Дата инициализации: " + collectionManager.getCreationDate());
        System.out.println("Количество элементов: " + collectionManager.getSize());

         */
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 