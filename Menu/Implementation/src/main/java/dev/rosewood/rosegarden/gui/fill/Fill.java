package dev.rosewood.rosegarden.gui.fill;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import org.bukkit.configuration.ConfigurationSection;

public interface Fill {

    AbstractItemProvider getItem(Context context, int slot);

    AbstractSlotProvider getSlots(Context context);

    void write(ConfigurationSection section);

}
