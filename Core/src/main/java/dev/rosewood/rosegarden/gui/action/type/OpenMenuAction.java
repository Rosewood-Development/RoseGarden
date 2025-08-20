package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.Optional;

/**
 * An action that opens a new menu.
 */
public class OpenMenuAction extends AbstractAction {

    public static final String ID = "open-menu";

    protected final String menu;
    protected final int page;

    // Code Constructors

    public OpenMenuAction(String menu) {
        super(ID);

        this.menu = menu;
        this.page = 1;
    }

    public OpenMenuAction(String menu, int page) {
        super(ID);

        this.menu = menu;
        this.page = page;
    }

    // Config Constructor
    public OpenMenuAction(ConfigurationSection section) {
        super(ID, section);

        if (section.isConfigurationSection(ID)) {
            this.menu = section.getString(ID + ".menu");
            this.page = section.getInt(ID + ".page", 1);
        } else {
            this.menu = section.getString(ID);
            this.page = 1;
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        if (this.page != 1) {
            if (this.menu != null)
                section.set(ID + ".menu", this.menu);

            section.set(ID + ".page", page);
        } else {
            section.set(ID, this.menu);
        }
    }

    @Override
    public void run(Context context) {
        // Open the new menu if everything is valid.
        Optional<Player> player = context.get(Parameters.PLAYER);
        Optional<RosePlugin> plugin = context.get(Parameters.PLUGIN);
        if (!player.isPresent() || !plugin.isPresent() || this.menu == null)
            return;

        plugin.get().getManager(AbstractGuiManager.class).open(this.menu, player.get(), this.page);
    }

    public String getMenu() {
        return this.menu;
    }

    // Static Constructors

    public static OpenMenuAction of(String menu) {
        return new OpenMenuAction(menu);
    }

    public static OpenMenuAction of(String menu, int page) {
        return new OpenMenuAction(menu, page);
    }

}
