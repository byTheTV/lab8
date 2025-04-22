package Client.commandManagers;

import exceptions.BuildObjectException;

/**
 * Интерфейс для реализации команд.
 */
public interface CommandInterface {

    /**
     * Выполняет команду.
     *
     * @throws BuildObjectException если происходит ошибка при построении объекта.
     */
    void execute() throws BuildObjectException;

    /**
     * Base method for show command name
     *
     * @return command name
     */
    String getName();

    /**
     * Base method for show command description.
     *
     * @return command description
     */
    String getDescr();

    /**
     * Checks if the provided argument is valid for the command.
     *
     * @param argument the argument to check for validity
     * @return true if the argument is valid, false otherwise
     */
    boolean checkArgument(Object argument);
}
