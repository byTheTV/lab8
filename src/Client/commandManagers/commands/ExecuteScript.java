package Client.commandManagers.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import Client.commandManagers.Command;
import Client.commandManagers.CommandManager;
import Client.commandManagers.CommandMode;
import Client.exceptions.RecursiveScriptException;

public class ExecuteScript extends Command {
    private static final Set<String> executedScripts = new HashSet<>();
    private final CommandManager commandManager;

    public ExecuteScript(CommandManager commandManager) {
        super(true);
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

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            CommandMode previousMode = commandManager.getCurrentMode();
            Scanner previousScanner = commandManager.getScanner();
            
            commandManager.setCurrentMode(CommandMode.NonUserMode);
            commandManager.setScanner(fileScanner);
            
            try {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        String[] args = line.split("\\s+", 2);
                        commandManager.executeCommand(args);
                    }
                }
            } finally {
                commandManager.setCurrentMode(previousMode);
                commandManager.setScanner(previousScanner);
                executedScripts.remove(fileName);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + fileName);
        }
    }

    @Override
    public boolean checkArgument(Object argument) {
        return argument instanceof String && !((String) argument).isEmpty();
    }
}