package Client.commandManagers.commands;

import Client.commandManagers.Command;

public class RemoveById extends Command {
    public RemoveById() {
        super(true);
    }

    @Override
    public String getName() {
        return "remove_by_id";
    }

    @Override
    public String getDescr() {
        return "удалить элемент из коллекции по его id";
    }

    @Override
    public void execute() {
        try {
            /*
            int id = Integer.parseInt(argument);
            if (collectionManager.removeById(id)) {
                System.out.println("Элемент успешно удален");
            } else {
                System.out.println("Элемент с указанным id не найден");
            }

             */
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом");
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        try {
            Integer.parseInt((String) argument);
            return true;
        } catch (NumberFormatException | ClassCastException e) {
            return false;
        }
    }
} 