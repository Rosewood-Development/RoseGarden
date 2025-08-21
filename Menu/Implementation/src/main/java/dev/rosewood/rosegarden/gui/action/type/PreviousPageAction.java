package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.RoseMenu;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PreviousPageAction extends AbstractAction {

    public static final String ID = "previous-page";

    // Code Constructors

    public PreviousPageAction() {
        super(ID);
    }

    // Config Constructors

    public PreviousPageAction(ConfigurationSection section) {
        super(ID, section);
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(ID, true);
    }

    @Override
    public void run(Context context) {
        Optional<Player> player = context.get(Parameters.PLAYER);
        Optional<RosePlugin> plugin = context.get(Parameters.PLUGIN);
        Optional<RoseMenu> menu = context.get(Parameters.MENU);
        if (!menu.isPresent() || !plugin.isPresent() || !player.isPresent())
            return;

        int currentPage = menu.get().getPageIndex();
        plugin.get().getManager(AbstractGuiManager.class)
                .open(menu.get().getWrapper().getId(), player.get(), currentPage - 1, context);
    }

    // Static Constructors

    public static PreviousPageAction of() {
        return new PreviousPageAction();
    }

}
