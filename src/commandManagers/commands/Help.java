package commandManagers.commands;

import commandManagers.Command;

/**
 * Команда help выводит справку по доступным командам.
 */
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
        // ANSI escape-последовательности для цвета
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_GREEN = "\u001B[32m";

        System.out.println("================================================");
        System.out.println(ANSI_GREEN + "Доступные команды:" + ANSI_RESET);
        System.out.println("help : вывести справку по доступным командам");
        System.out.println("info : вывести информацию о коллекции");
        System.out.println("show : вывести все элементы коллекции");
        System.out.println("add : добавить новый элемент в коллекцию");
        System.out.println("update_id {id} : обновить значение элемента коллекции по id");
        System.out.println("remove_by_id {id} : удалить элемент из коллекции по его id");
        System.out.println("clear : очистить коллекцию");
        System.out.println("save : сохранить коллекцию в файл");
        System.out.println("execute_script {file_name} : считать и исполнить скрипт из файла");
        System.out.println("exit : завершить программу (без сохранения)");
        System.out.println("head : вывести первый элемент коллекции");
        System.out.println("remove_head : вывести первый элемент коллекции и удалить его");
        System.out.println("remove_lower {element} : удалить из коллекции все элементы, меньшие чем заданный");
        System.out.println("average_of_transferred_students : вывести среднее значение transferredStudents для всех элементов коллекции");
        System.out.println("group_counting_by_form_of_education : сгруппировать элементы коллекции по formOfEducation и вывести количество элементов в каждой группе");
        System.out.println("print_field_ascending_group_admin : вывести значения поля groupAdmin всех элементов в порядке возрастания");
        System.out.println("================================================");
    }

    @Override
    public boolean checkArgument(Object argument) {
        return true;
    }
} 