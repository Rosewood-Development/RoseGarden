package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * An action that opens a new menu.
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: open_menu
 *         menu: some_menu
 *     }
 * </pre>
 * OR<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       open-menu:
 *         0:
 *           type: open_menu
 *           menu: some_menu
 *           page: 1
 *           carry-context: true
 *     }
 * </pre>
 */
public class OpenMenuAction extends AbstractAction {

    public static final String ID = "open_menu";
    public static final String MENU = "menu";
    public static final String PAGE = "page";
    public static final String CARRY_CONTEXT = "carry-context";

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

        this.menu = section.getString(MENU);
        this.page = section.getInt(PAGE, 1);
        this.carryContext = section.getBoolean(CARRY_CONTEXT, false);
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(MENU, this.menu);

        if (this.page != 1)
            section.set(PAGE, this.page);

        if (this.carryContext)
            section.set(CARRY_CONTEXT, true);
    }

    @Override
    public void run(Context context) {
        // Open the new menu if everything is valid.
        Optional<Player> player = context.get(Parameters.PLAYER);
        Optional<RosePlugin> plugin = context.get(Parameters.PLUGIN);
        if (player.isEmpty() || plugin.isEmpty() || this.menu == null)
            return;

        plugin.get().getManager(AbstractGuiManager.class)
                .open(this.menu, player.get(), this.page, this.carryContext ? context : Context.empty());
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
