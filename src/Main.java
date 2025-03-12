import collectionManagers.StudyGroupCollectionManager;
import commandManagers.CommandExecutor;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


/**
 * Точка входа в приложение.
 * <p>
 * Приложение ожидает путь к XML-файлу, содержащему данные, переданный в качестве аргумента командной строки.
 * Если аргумент не передан или является пустой строкой, будет использоваться файл по умолчанию со следующим именем: {@code default.xml}.
 * После инициализации менеджера коллекции происходит загрузка данных и запуск командного обработчика в режиме CLI.
 * </p>
 * <p>
 * Пример запуска:
 * <blockquote><pre>
 * java -cp . Main study_groups.xml
 * cd "d:\.Javaproj\lab5\src\" ; if ($?) { javac Main.java } ; if ($?) { java Main study_groups.xml }
 * gradle runProgram --console=plain
 *
 * </pre></blockquote>
 * </p>
 *
 * @author T.V.
 * @version 1.0
 */
public class Main {

    /**
     * Основной метод приложения.
     *
     * @param args Аргументы командной строки, где ожидается, что первый аргумент содержит путь к XML-файлу.
     *             Если аргументы отсутствуют или первый аргумент пуст, используется {@code study_groups.xml}.
     */
    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
            // Проверка аргументов командной строки.
            // Если имя файла не передано или передан пустой буфер, используем файл по умолчанию.
            String dataFile = "src/resourses/study_groups.xml";
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
            
            // Передаём менеджер коллекции в CommandExecutor и запускаем выполнение команд в CLI режиме
            CommandExecutor executor = new CommandExecutor(manager);
            executor.startExecuting(System.in);
            
        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }
} 

/*
javadoc -d doc -sourcepath src -subpackages models:collectionManagers:commandManagers:fileLogic

jetbra.in/s
*/