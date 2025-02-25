package commandManagers;

import collectionManagers.StudyGroupCollectionManager;
import exceptions.BuildObjectException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Класс {@code CommandExecutor} обрабатывает ввод и выполнение команд,
 * направленных на управление коллекцией {@code StudyGroup}.
 */
public class CommandExecutor {

    private final StudyGroupCollectionManager collectionManager;

    /**
     * Конструктор для создания исполнитель команд.
     *
     * @param collectionManager менеджер коллекции, с которым работают команды.
     */
    public CommandExecutor(StudyGroupCollectionManager collectionManager) {
        if (collectionManager == null) {
            throw new IllegalArgumentException("StudyGroupCollectionManager не должен быть null");
        }
        this.collectionManager = collectionManager;
    }

    /**
     * Запускает выполнение команд, считывая их из заданного входного потока.
     * Перед началом работы сразу вызывается команда help для вывода справки по доступным командам.
     *
     * @param in поток ввода для считывания команд.
     */
    public void startExecuting(InputStream in) {
        // Передаём уже существующий collectionManager в CommandManager
        try (Scanner scanner = new Scanner(in)) {
            // Передаём уже существующий collectionManager в CommandManager
            CommandManager commandManager = new CommandManager(scanner, collectionManager);
            
            // Вызываем команду help сразу при запуске, чтобы вывести список всех доступных команд
            if (commandManager.getCommandMap().containsKey("help")) {
                try {
                    commandManager.getCommandMap().get("help").execute();
                } catch (BuildObjectException e) {
                    System.err.println("Ошибка выполнения команды help: " + e.getMessage());
                }
            }
            
            System.out.println("Командный режим запущен. Введите команды (для выхода введите 'exit'):");
            while (scanner.hasNextLine()) {
                System.out.print("> ");
                String inputLine = scanner.nextLine().trim();
                if (inputLine.isEmpty()) continue;
                String[] args = inputLine.split("\\s+");
                if ("exit".equalsIgnoreCase(args[0])) {
                    System.out.println("Выход из командного режима.");
                    break;
                }
                try {
                    commandManager.executeCommand(args);
                } catch (Exception e) {
                    System.err.println("Ошибка выполнения команды: " + e.getMessage());
                }
            }
        }
    }
}
