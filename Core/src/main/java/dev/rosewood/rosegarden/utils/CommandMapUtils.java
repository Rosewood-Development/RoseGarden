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

    /**
     * Registers a command to the Bukkit command map
     *
     * @param prefix The command namespace prefix
     * @param command The command
     */
    public static void registerCommand(String prefix, Command command) {
        registerCommand(prefix, command, false);
    }

    /**
     * Registers a command to the Bukkit command map
     *
     * @param prefix The command namespace prefix
     * @param command The command
     * @param force If true, will unregister any command at the main namespace that has the same name before being inserted
     * @return true if any command was unregistered from the command map, false otherwise
     */
    public static boolean registerCommand(String prefix, Command command, boolean force) {
        boolean removed = false;
        if (force) {
            Map<String, Command> knownCommands = getKnownCommands();
            removed |= knownCommands.remove(command.getName()) != null;
            for (String alias : command.getAliases())
                removed |= knownCommands.remove(alias) != null;
        }

        getCommandMap().register(prefix, command);
        return removed;
    }

    /**
     * Unregisters a command from the Bukkit command map
     *
     * @param command The command
     */
    public static void unregisterCommand(Command command) {
        Map<String, Command> knownCommands = getKnownCommands();
        for (Map.Entry<String, Command> entry : new HashMap<>(knownCommands).entrySet())
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

    @SuppressWarnings("unchecked")
    private static Map<String, Command> getKnownCommands() {
        try {
            return (Map<String, Command>) knownCommandsField.get(getCommandMap());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

}
