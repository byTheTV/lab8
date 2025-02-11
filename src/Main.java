
import commandManagers.CommandExecutor;
import commandManagers.CommandMode;

public class Main {
    public static void main(String[] args) {
        try {
            CommandExecutor executor = new CommandExecutor();
            System.out.println("Добро пожаловать! Введите help для получения списка команд.");
            
            // Запускаем обработку команд в интерактивном режиме
            executor.startExecuting(System.in, CommandMode.CLI_UserMode);
            
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 