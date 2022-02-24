package dev.rosewood.rosegarden.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

public final class CommandMapUtils {

    private static final Field bukkitCommandMapField;
    private static final Field knownCommandsField;

    static {
        try {
            bukkitCommandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMapField.setAccessible(true);

            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unable to access commandMap or knownCommands field", e);
        }
    }

    public static void registerCommand(String prefix, Command command) {
        getCommandMap().register(prefix, command);
    }

    public static void unregisterCommand(Command command) {
        Map<String, Command> knownCommands = getKnownCommands();
        for (Map.Entry<String, Command> entry : new HashMap<>(getKnownCommands()).entrySet())
            if (entry.getValue().equals(command))
                knownCommands.remove(entry.getKey());

        command.unregister(getCommandMap());
    }

    private static CommandMap getCommandMap() {
        try {
            return (CommandMap) bukkitCommandMapField.get(Bukkit.getServer());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access Bukkit CommandMap", e);
        }
    }

    private static Map<String, Command> getKnownCommands() {
        try {
            return (Map<String, Command>) knownCommandsField.get(getCommandMap());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
