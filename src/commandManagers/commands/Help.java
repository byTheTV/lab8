package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;

public class Help extends Command {
    public Help() {
        super(false, null);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescr() {
        return "вывести справку по доступным командам";
    }

    @Override
    public void execute() {
        System.out.println("Доступные команды:");
        // Здесь нужно будет добавить вывод всех команд и их описаний
        System.out.println("help : " + getDescr());
        System.out.println("info : вывести информацию о коллекции");
        System.out.println("show : вывести все элементы коллекции");
        System.out.println("add : добавить новый элемент в коллекцию");
        System.out.println("update_id {id} : обновить значение элемента коллекции по id");
        System.out.println("remove_by_id {id} : удалить элемент из коллекции по его id");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script {file_name} : считать и исполнить скрипт из файла");
        System.out.println("exit : завершить программу");
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 