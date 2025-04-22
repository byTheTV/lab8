package Client.commandManagers.commands;

import Client.commandManagers.Command;

public class Show extends Command {
    public Show() {
        super(false);
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
        /*
        if (collectionManager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        collectionManager.getCollection().forEach(System.out::println);

         */
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 