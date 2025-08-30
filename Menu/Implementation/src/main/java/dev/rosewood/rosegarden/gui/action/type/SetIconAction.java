package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action that places an item in a slot.<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: set_icon
 *         item:
 *           type: item
 *           material: stone
 *     }
 * </pre>
 */
public class SetIconAction extends AbstractAction {

    public static final String ID = "set_icon";

    protected final Icon icon;

    // Code Constructors

    public SetIconAction(Icon icon) {
        super(ID);

        this.icon = icon;
    }

    // Config Constructors

    public SetIconAction(ConfigurationSection section) {
        super(ID, section);

        this.icon = new Icon();
        RoseMenuWrapper.loadIconData(section, icon);
    }

    @Override
    public void write(ConfigurationSection section) {
        RoseMenuWrapper.writeIconData(section, icon);
    }

    @Override
    public void run(Context context) {
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (view.isEmpty())
            return;

        List<Integer> slots = new ArrayList<>();
        Optional<AbstractSlotProvider> slot = icon.getProvider(Providers.SLOT);
        if (slot.isEmpty())
            context.get(Parameters.SLOT).ifPresent(slots::add);

        for (int slotIndex : slots) {
            view.get().getActiveIcons().put(slotIndex, this.icon);
            view.get().refresh(context, slotIndex);
        }

    }

    public Icon getIcon() {
        return this.icon;
    }

    // Static Constructors

    public static SetIconAction of(Icon icon) {
        return new SetIconAction(icon);
    }

    public static SetIconAction of(Item item) {
        return new SetIconAction(new Icon(item));
    }

}
