package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action that closes the currently open menu.<br>
 *
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: close_menu
 *     }
 * </pre>
 */
public class CloseMenuAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "close_menu";

    // Code Constructors

    public CloseMenuAction() {
        super(ID);
    }

    // Config Constructors

    public CloseMenuAction(ConfigurationSection section) {
        super(ID, section);
    }

    @Override
    public void run(Context context) {
        // Close the menu for the player, if the view is present.
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (view.isEmpty())
            return;

        view.get().close();
    }

    // Static Constructors

    public static CloseMenuAction of() {
        return new CloseMenuAction();
    }

}
