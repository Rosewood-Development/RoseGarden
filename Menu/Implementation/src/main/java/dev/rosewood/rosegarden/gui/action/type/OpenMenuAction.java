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
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       open-menu: some-menu
 *     }
 * </pre>
 * OR<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       open-menu:
 *         menu: some-menu
 *         page: 1
 *         carry-context: true
 *     }
 * </pre>
 */
public class OpenMenuAction extends AbstractAction {

    public static final String ID = "open-menu";

    protected final String menu;
    protected final int page;
    protected final boolean carryContext;

    // Code Constructors

    public OpenMenuAction(String menu) {
        super(ID);

        this.menu = menu;
        this.page = 1;
        this.carryContext = false;
    }

    public OpenMenuAction(String menu, int page) {
        super(ID);

        this.menu = menu;
        this.page = page;
        this.carryContext = false;
    }

    public OpenMenuAction(String menu, int page, boolean carryContext) {
        super(ID);

        this.menu = menu;
        this.page = page;
        this.carryContext = carryContext;
    }

    // Config Constructors

    public OpenMenuAction(ConfigurationSection section) {
        super(ID, section);

        if (section.isConfigurationSection(ID)) {
            this.menu = section.getString(ID + ".menu");
            this.page = section.getInt(ID + ".page", 1);
            this.carryContext = section.getBoolean(ID + ".carry-context", false);
        } else {
            this.menu = section.getString(ID);
            this.page = 1;
            this.carryContext = false;
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        if (this.page != 1) {
            if (this.menu != null)
                section.set(ID + ".menu", this.menu);

            section.set(ID + ".page", page);
            if (this.carryContext)
                section.set(ID + ".carry-context", true);
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

        plugin.get().getManager(AbstractGuiManager.class).open(this.menu, player.get(), this.page, this.carryContext ? context : Context.empty());
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
