import collectionManagers.StudyGroupCollectionManager;
import commandManagers.CommandExecutor;

public class Main {
    public static void main(String[] args) {
        try {
            // Проверка буфера аргументов. Если имя файла не передано или передан пустой буфер, используем файл по умолчанию.
            String dataFile = "src/study_groups.xml";
            if (args.length < 1 || args[0].trim().isEmpty()) {
                System.out.println("Имя файла не указано или передан пустой буфер. Используем файл по умолчанию: " + dataFile);
            } else {
                dataFile = args[0];
            }
            
            // Добавляем вывод текущей директории для отладки
            System.out.println("Текущая рабочая директория: " + System.getProperty("user.dir"));
            
            // Добавляем проверку существования файла
            java.io.File file = new java.io.File(dataFile);
            if (!file.exists()) {
                System.err.println("Предупреждение: Файл не найден по пути: " + file.getAbsolutePath());
            }
            
            // Создание менеджера коллекции и загрузка данных из файла
            StudyGroupCollectionManager manager = new StudyGroupCollectionManager();
            System.out.println("Попытка инициализации данных из файла: " + dataFile);
            manager.initializeData(dataFile);
            boolean loadSuccess = manager.load();
            if (loadSuccess) {
                System.out.println("\nКоллекция успешно загружена из файла: " + dataFile);
                System.out.println("Информация о коллекции:");
                System.out.println("Размер коллекции: " + manager.getSize());
                System.out.println("Тип коллекции: " + manager.getCollectionType());
                System.out.println("Дата создания: " + manager.getCreationDate());
                System.out.println("Среднее число переведенных студентов: " + manager.getAverageOfTransferredStudents());
            } else {
                System.err.println("Ошибка: Не удалось загрузить коллекцию из файла: " + dataFile);
            }
            
            // Передаём менеджер коллекции в CommandExecutor
            CommandExecutor executor = new CommandExecutor(manager);
            executor.startExecuting(System.in);
            
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
} 