package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action that refreshes the open menu.
 *
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: refresh
 *     }
 * </pre>
 */
public class RefreshAction extends AbstractAction {

    public static final String ID = "refresh";

    // Code Constructors

    public RefreshAction() {
        super(ID);
    }

    // Config Constructor
    public RefreshAction(ConfigurationSection section) {
        super(ID, section);
    }

    @Override
    public void run(Context context) {
        // Refresh the menu if it is valid.
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (view.isEmpty())
            return;

        view.get().refresh();
    }

    // Static Constructors

    public static RefreshAction of() {
        return new RefreshAction();
    }

}
