package commandManagers.commands;

import commandManagers.Command;
import collectionManagers.StudyGroupCollectionManager;
import models.StudyGroup;
import java.util.Collection;

/**
 * Команда average_of_transferred_students: выводит среднее значение поля transferredStudents.
 */
public class AverageOfTransferredStudents extends Command {

    public AverageOfTransferredStudents(StudyGroupCollectionManager collectionManager) {
        super(false, collectionManager);
    }
    
    @Override
    public String getName() {
        return "average_of_transferred_students";
    }
    
    @Override
    public String getDescr() {
        return "вывести среднее значение поля transferredStudents для всех элементов коллекции";
    }
    
    @Override
    public void execute() {
        Collection<StudyGroup> collection = collectionManager.getCollection();
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста");
            return;
        }
        
        double sum = 0;
        int count = 0;
        for (StudyGroup sg : collection) {
            sum += sg.getTransferredStudents();
            count++;
        }
        
        double average = sum / count;
        System.out.println("Среднее значение transferredStudents: " + average);
    }
    
    @Override
    public boolean checkArgument(Object argument) {
        // Команде не требуется аргумент
        return true;
    }
} 