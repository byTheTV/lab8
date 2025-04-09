package client.commandManagers.commands;

import client.commandManagers.Command;

public class Clear extends Command {
    public Clear() {
        super(false);
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
        //
        // Fix here
        // collectionManager.clear();
        System.out.println("Коллекция очищена");
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 