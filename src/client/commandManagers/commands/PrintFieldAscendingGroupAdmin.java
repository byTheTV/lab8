package Client.commandManagers.commands;

import Client.commandManagers.Command;

/**
 * Команда print_field_ascending_group_admin: выводит значения поля groupAdmin всех элементов 
 * коллекции в порядке возрастания. Сортировка производится по имени администратора.
 */
public class PrintFieldAscendingGroupAdmin extends Command {

    public PrintFieldAscendingGroupAdmin() {
        super(false);
    }
    
    @Override
    public String getName() {
        return "print_field_ascending_group_admin";
    }
    
    @Override
    public String getDescr() {
        return "вывести значения поля groupAdmin всех элементов в порядке возрастания";
    }
    
    @Override
    public void execute() {
        /*
        List<Person> admins = new ArrayList<>();
        for (StudyGroup sg : collectionManager.getCollection()) {
            admins.add(sg.getGroupAdmin());
        }
        
        // Сортировка по имени администратора
        admins.sort(Comparator.comparing(Person::getName));
        System.out.println("Список GroupAdmin в порядке возрастания:");
        for (Person admin : admins) {
            System.out.println(admin);
        }

         */
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 