package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import java.util.List;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

public class SetItemAction extends AbstractAction {

    public static final String ID = "set-item";

    protected final Icon icon;

    // Code Constructors

    public SetItemAction(Icon icon) {
        super(ID);

        this.icon = icon;
    }

    // Config Constructors

    public SetItemAction(ConfigurationSection section) {
        super(ID, section);

        this.icon = new Icon();
        RoseMenuWrapper.loadIconData(section.getConfigurationSection(ID), icon);
    }

    @Override
    public void write(ConfigurationSection section) {
        RoseMenuWrapper.writeIconData(section.createSection(ID), icon);
    }

    @Override
    public void run(Context context) {
        Optional<MenuView> view = context.get(Parameters.VIEW);
        if (view.isEmpty())
            return;

        Optional<AbstractSlotProvider> slot = icon.getProvider(Providers.SLOT);
        if (slot.isEmpty())
            return;

        List<Integer> slots = slot.get().get(context);
        for (int slotIndex : slots)
            view.get().getActiveIcons().put(slotIndex, this.icon);
    }

    public Icon getIcon() {
        return this.icon;
    }

    // Static Constructors

    public static SetItemAction of(Icon icon) {
        return new SetItemAction(icon);
    }

}
