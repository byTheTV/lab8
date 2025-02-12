package commandManagers.commands;

import commandManagers.Command;
import commandManagers.CommandManager;
import exceptions.RecursiveScriptException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScript extends Command {
    private static final Set<String> executedScripts = new HashSet<>();
    private final CommandManager commandManager;

    public ExecuteScript(CommandManager commandManager) {
        super(true, null); // требует аргумент, но не требует collectionManager
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescr() {
        return "считать и исполнить скрипт из указанного файла";
    }

    @Override
    public void execute() {
        String fileName = (String) this.argument;
        
        if (!executedScripts.add(fileName)) {
            throw new RecursiveScriptException("Обнаружен рекурсивный вызов скрипта: " + fileName);
        }

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] args = line.split("\\s+", 2);
                    commandManager.executeCommand(args);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
        } finally {
            executedScripts.remove(fileName);
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof String && !((String) argument).isEmpty();
    }
} 