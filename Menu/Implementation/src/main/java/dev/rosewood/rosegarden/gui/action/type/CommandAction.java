package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * An action that runs commands.<br>
 *
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       commands:
 *         - command
 *     }
 * </pre>
 * OR<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       commands:
 *         player:
 *           - command
 *         server:
 *           - command
 *     }
 * </pre>
 */
public class CommandAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "commands";

    protected final List<String> playerCommands;
    protected final List<String> consoleCommands;

    // Code Constructors

    public CommandAction(String... commands) {
        super(ID);

        this.playerCommands = new ArrayList<>();
        this.consoleCommands = Arrays.asList(commands);
    }

    public CommandAction(List<String> playerCommands, List<String> consoleCommands) {
        super(ID);

        this.playerCommands = playerCommands;
        this.consoleCommands = consoleCommands;
    }

    // Config Constructors

    public CommandAction(ConfigurationSection section) {
        super(ID, section);

        if (section.isConfigurationSection(ID)) {
            // Grab a list of player and/or console commands.
            this.playerCommands = section.contains(ID + ".player") ?
                    section.getStringList(ID + ".player") :
                    new ArrayList<>();
            this.consoleCommands = section.contains(ID + ".console") ?
                    section.getStringList(ID + ".console") :
                    new ArrayList<>();
        } else if (section.isList(ID)) {
            // If the section is a list, use those as console commands.
            this.playerCommands = new ArrayList<>();
            this.consoleCommands = section.getStringList(ID);
        }  else {
            // Initialise lists anyway if we fail to grab any data.
            this.playerCommands = new ArrayList<>();
            this.consoleCommands = new ArrayList<>();
        }
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        // If there are player commands, write them to the file.
        if (!this.playerCommands.isEmpty())
            section.set(ID + ".player", this.playerCommands);

        // If there are console commands, write them to a file.
        // Put them in a section if there are player commands too.
        if (!this.consoleCommands.isEmpty()) {
            if (!this.playerCommands.isEmpty())
                section.set(ID + ".console", this.consoleCommands);
            else
                section.set(ID, this.consoleCommands);
        }
    }

    @Override
    public void run(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);

        // Run the console commands when activated.
        if (!this.consoleCommands.isEmpty())
            this.consoleCommands.forEach(cmd ->
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPIHook.applyPlaceholders(player.orElse(null), cmd)));

        // Run the player commands when activated, if the player is valid.
        if (!this.playerCommands.isEmpty()) {
            if (player.isEmpty())
                return;

            this.playerCommands.forEach(cmd ->
                    player.get().performCommand(PlaceholderAPIHook.applyPlaceholders(player.get(), cmd)));
        }
    }

    public List<String> getPlayerCommands() {
        return this.playerCommands;
    }

    public List<String> getConsoleCommands() {
        return this.consoleCommands;
    }

    // Static Constructors

    public static CommandAction of(String... commands) {
        return new CommandAction(commands);
    }

    public static CommandAction of(List<String> playerCommands, List<String> consoleCommands) {
        return new CommandAction(playerCommands, consoleCommands);
    }

}
