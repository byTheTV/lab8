package commandManagers;

import commandManagers.commands.*;
import exceptions.BuildObjectException;
import exceptions.CommandInterruptedException;
import exceptions.UnknownCommandException;
import java.util.*;
import collectionManagers.StudyGroupCollectionManager;

/**
 The CommandManager class is responsible for managing all available commands in the application.
 It provides a static method getCommandMap() that returns a HashMap of all commands where the key is
 the command name and the value is an instance of the corresponding Command subclass.
 */
public class CommandManager {

    /**
     A HashMap object that stores all available commands in the application. The key is the command name
     and the value is an instance of the corresponding Command subclass.
     */
    private LinkedHashMap<String, Command> commandMap;
    private final Scanner scanner;

    /**
     * Default command manager setup.
     *
     * @since 1.0
     */
    public CommandManager(CommandMode mode, Scanner scanner) {
        this.scanner = scanner;
        commandMap = new LinkedHashMap<>();
        StudyGroupCollectionManager collectionManager = new StudyGroupCollectionManager();

        commandMap.put("help", new Help());
        commandMap.put("info", new Info(collectionManager));
        commandMap.put("show", new Show(collectionManager));
        commandMap.put("add", new Add(collectionManager, scanner));
        commandMap.put("update_id", new UpdateId(collectionManager, scanner));
        commandMap.put("remove_by_id", new RemoveById(collectionManager));
        commandMap.put("clear", new Clear(collectionManager));
       /*
        commandMap.put("save", new Save(collectionManager));
        commandMap.put("execute_script", new ExecuteScript());
        commandMap.put("exit", new Exit());
        commandMap.put("head", new Head(collectionManager));
        commandMap.put("remove_head", new RemoveHead(collectionManager));
        commandMap.put("remove_lower", new RemoveLower(collectionManager, scanner));
        commandMap.put("average_of_transferred_students", new AverageOfTransferredStudents(collectionManager));
        commandMap.put("group_counting_by_form_of_education", new GroupCountingByFormOfEducation(collectionManager));
        commandMap.put("print_field_ascending_group_admin", new PrintFieldAscendingGroupAdmin(collectionManager));
    */
    }

    /**
     Returns the commandMap HashMap that stores all available commands in the application.
     @return the HashMap of all commands where the key is the command name and the value is an instance
     of the corresponding Command subclass.
     */
    public HashMap<String, Command> getCommandMap() {
        return commandMap;
    }

    /**
     * Universe method for command executing.
     */
    public void executeCommand(String[] args) {
        try {
            if (args.length > 1)
                Optional.ofNullable(commandMap.get(args[0])).orElseThrow(() -> new UnknownCommandException("\nКоманды " + args[0] + " не обнаружено :( ")).setArgument(args[1]);
            Optional.ofNullable(commandMap.get(args[0])).orElseThrow(() -> new UnknownCommandException("\nКоманды " + args[0] + " не обнаружено :( ")).execute();
        } catch (IllegalArgumentException | NullPointerException | NoSuchElementException e) {
            System.err.println("Выполнение команды пропущено из-за неправильных предоставленных аргументов! (" + e.getMessage() + ")");
            throw new CommandInterruptedException(e);
        } catch (BuildObjectException | UnknownCommandException e) {
            System.err.println(e.getMessage());
            throw new CommandInterruptedException(e);
        } catch (Exception e) {
            System.err.println("В командном менеджере произошла непредвиденная ошибка! ");
            throw new CommandInterruptedException(e);
        }
    }
}
