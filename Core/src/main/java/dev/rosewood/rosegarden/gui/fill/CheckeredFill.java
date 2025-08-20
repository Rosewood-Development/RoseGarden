package dev.rosewood.rosegarden.gui.fill;

import dev.rosewood.rosegarden.gui.RoseMenu;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.FillProvider;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import dev.rosewood.rosegarden.gui.provider.slot.MultiSlotProvider;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CheckeredFill implements Fill {

    public static final String ID = "checkered";

    @Override
    public AbstractItemProvider getItem(Context context, int slot) {
        Optional<Icon> icon = context.get(Parameters.ICON);
        if (!icon.isPresent())
            return new ItemProvider(RoseItem.empty());

        return (AbstractItemProvider) icon.get().getProvider(Providers.ITEM).orElse(new ItemProvider(RoseItem.empty()));
    }

    @Override
    public AbstractSlotProvider getSlots(Context context) {
        Optional<RoseMenu> menu = context.get(Parameters.MENU);
        if (!menu.isPresent())
            return null;

        MultiSlotProvider provider = new MultiSlotProvider();

        // If the icon has slots, use those instead.
        Optional<Icon> icon = context.get(Parameters.ICON);
        if (icon.isPresent()) {
            Optional<AbstractSlotProvider> slotProvider = icon.get().getProvider(Providers.SLOT);
            if (slotProvider.isPresent()) {
                List<Integer> slots = slotProvider.get().get(context);
                Collections.sort(slots);

                for (int i = slots.get(0); i < slots.get(slots.size() - 1) + 1; i += 2)
                    provider.add(i);

                return provider;
            }
        }

        int size = menu.get().getSize();
        for (int i = 0; i < size + 1; i += 2)
            provider.add(i);

        return provider;
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(FillProvider.ID, ID);
    }

}
