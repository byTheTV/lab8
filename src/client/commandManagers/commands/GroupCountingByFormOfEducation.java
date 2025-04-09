package client.commandManagers.commands;

import client.commandManagers.Command;
import client.models.StudyGroup;
import client.models.FormOfEducation;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

/**
 * Команда group_counting_by_form_of_education: группирует элементы коллекции по значению поля formOfEducation
 * и выводит количество элементов в каждой группе.
 */
public class GroupCountingByFormOfEducation extends Command {

    public GroupCountingByFormOfEducation() {
        super(false);
    }
    
    @Override
    public String getName() {
        return "group_counting_by_form_of_education";
    }
    
    @Override
    public String getDescr() {
        return "сгруппировать элементы коллекции по значению поля formOfEducation, вывести количество элементов в каждой группе";
    }
    
    @Override
    public void execute() {

        /*
         Collection<StudyGroup> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        
        Map<FormOfEducation, Integer> grouping = new HashMap<>();
        for (StudyGroup sg : collection) {
            FormOfEducation form = sg.getFormOfEducation();
            grouping.put(form, grouping.getOrDefault(form, 0) + 1);
        }
        
        for (Map.Entry<FormOfEducation, Integer> entry : grouping.entrySet()) {
            System.out.println("Форма образования: " + entry.getKey() + " - " + entry.getValue() + " элементов");
        }

         */
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 