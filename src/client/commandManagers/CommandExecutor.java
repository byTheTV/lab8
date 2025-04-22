package Client.commandManagers;

import java.io.InputStream;
import java.util.Scanner;

import Client.network.TCPClient;
import Client.exceptions.BuildObjectException;


/**
 * Класс {@code CommandExecutor} обрабатывает ввод и выполнение команд,
 * направленных на управление коллекцией {@code StudyGroup}.
 */
public class CommandExecutor {
    private TCPClient client;
    /**
     * Конструктор для создания исполнитель команд.
     *
     */
    public CommandExecutor(TCPClient client) {
        this.client = client;
    }

    /**
     * Запускает выполнение команд, считывая их из заданного входного потока.
     * Перед началом работы сразу вызывается команда help для вывода справки по доступным командам.
     *
     * @param in поток ввода для считывания команд.
     */

    public void startExecuting(InputStream in) {
        try (Scanner scanner = new Scanner(in)) {
            CommandManager commandManager = new CommandManager(scanner, client);
            
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
