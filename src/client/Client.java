package Client;

import java.io.IOException;
import java.net.InetAddress;

import Client.commandManagers.CommandExecutor;
import Client.network.TCPClient;


/**
 * Точка входа в клиентское приложение.
 * <p>
 * Приложение подключается к серверу по указанному адресу и порту.
 * По умолчанию используется localhost:5555.
 * </p>
 *
 * @author T.V.
 */
public class Client {
    /**
     * Основной метод клиентского приложения.
     *
     * @param args Аргументы командной строки (в данной версии не используются)
     */
    public static void main(String[] args) {
        try {

            // Параметры подключения по умолчанию
            String host = "localhost";
            int port = 55555;

            // Проверка аргументов командной строки
            if (args.length >= 1 && !args[0].trim().isEmpty()) {
                host = args[0];
            }
            if (args.length >= 2 && !args[1].trim().isEmpty()) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Предупреждение: Неверный формат порта. Используется порт по умолчанию: " + port);
                }
            }

            System.out.println("Параметры подключения: " + host + ":" + port);
            System.out.println("Текущая рабочая директория: " + System.getProperty("user.dir"));

            // Проверка доступности хоста
            try {
                InetAddress.getByName(host);
            } catch (IOException e) {
                System.err.println("Предупреждение: Хост " + host + " недоступен.");
            }

            // Создание клиента и подключение к серверу
            System.out.println("Попытка подключения к серверу: " + host + ":" + port);
            TCPClient client = new TCPClient(InetAddress.getByName(host), port);
            boolean connectionSuccess = client.isConnected(); // Assuming TCPClient has an isConnected() method
            if (connectionSuccess) {
                System.out.println("\nУспешное подключение к серверу: " + host + ":" + port);
                System.out.println("Информация о подключении:");
                System.out.println("Хост: " + host);
                System.out.println("Порт: " + port);
                System.out.println("Статус: Активно");
            } else {
                System.err.println("Ошибка: Не удалось установить соединение с сервером: " + host + ":" + port);
                return;
            }

            // Создание CommandExecutor и запуск обработки команд
            CommandExecutor executor = new CommandExecutor(client);
            System.out.println("Запуск обработки команд...");
            executor.startExecuting(System.in);

            // Закрытие клиента
            client.close();
        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
}