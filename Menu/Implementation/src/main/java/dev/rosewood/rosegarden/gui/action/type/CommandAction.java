package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
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
 *       0:
 *         type: command
 *         server-command: command
 *     }
 * </pre>
 * OR<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: command
 *         player-command: command
 *     }
 * </pre>
 */
public class CommandAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "command";

    protected final String command;
    protected final boolean playerCommand;

    // Code Constructors

    public CommandAction(boolean player, String command) {
        super(ID);

        this.command = command;
        this.playerCommand = player;
    }

    // Config Constructors

    public CommandAction(ConfigurationSection section) {
        super(ID, section);

        if (section.contains("player-command")) {
            this.playerCommand = true;
            this.command = section.getString("player-command");
        } else {
            this.playerCommand = false;
            this.command = section.getString("server-command");
        }
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        section.set((this.playerCommand ? "player-command" : "server-command"), this.command);
    }

    @Override
    public void run(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);

        String command = PlaceholderAPIHook.applyPlaceholders(player.orElse(null), this.command);
        if (this.playerCommand) {
            player.ifPresent(value -> value.performCommand(command));
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isPlayerCommand() {
        return this.playerCommand;
    }

    // Static Constructors

    public static CommandAction of(boolean player, String command) {
        return new CommandAction(player, command);
    }

}
