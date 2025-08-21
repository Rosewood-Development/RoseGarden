package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Optional;

/**
 * An action that closes the currently open menu.
 *
 * Config:
 * trigger-type:
 *   close-menu: true
 */
public class CloseMenuAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "close-menu";

    // Code Constructors

    public CloseMenuAction() {
        super(ID);
    }

    // Config Constructors

    public CloseMenuAction(ConfigurationSection section) {
        super(ID, section);
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        section.set(ID, true);
    }

    @Override
    public void run(Context context) {
        // Close the menu for the player, if the view is present.
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (!view.isPresent())
            return;

        view.get().close();
    }

    // Static Constructors

    public static CloseMenuAction of() {
        return new CloseMenuAction();
    }

}
