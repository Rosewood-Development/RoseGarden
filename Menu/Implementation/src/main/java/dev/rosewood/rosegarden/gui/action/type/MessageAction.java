package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.HexUtils;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * An action that sends a message to the player.<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: message
 *         message: "&esome message"
 *     }
 * </pre>
 */
public class MessageAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "message";

    protected final String message;

    // Code Constructors

    public MessageAction(String message) {
        super(ID);

        this.message = message;
    }

    // Config Constructors

    public MessageAction(ConfigurationSection section) {
        super(ID, section);

        this.message = section.getString(ID);
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        section.set(ID, this.message);
    }

    @Override
    public void run(Context context) {
        // Send the message if the player is valid.
        Optional<Player> player = context.get(Parameters.PLAYER);
        if (player.isEmpty() || this.message == null)
            return;

        String formattedMessage = HexUtils.colorify(this.message);
        player.get().sendMessage(PlaceholderAPIHook.applyPlaceholders(player.get(), formattedMessage));
    }

    public String getMessage() {
        return this.message;
    }

    // Static Constructors

    public static MessageAction of(String message) {
        return new MessageAction(message);
    }

}
