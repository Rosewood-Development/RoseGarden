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
        getCommandMap().register(prefix, command);
    }

    /**
     * Registers a command to the Bukkit command map
     *
     * @param prefix The command namespace prefix
     * @param command The command
     * @param force If true, will unregister any command at the main namespace that has the same name before being inserted
     */
    public static void registerCommand(String prefix, Command command, boolean force) {
        if (force)
            getKnownCommands().remove(command.getName());
        getCommandMap().register(prefix, command);
    }

    /**
     * Unregisters a command from the Bukkit command map
     *
     * @param command The command
     */
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
