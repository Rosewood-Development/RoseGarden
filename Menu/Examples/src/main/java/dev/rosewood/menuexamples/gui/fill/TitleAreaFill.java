package dev.rosewood.menuexamples.gui.fill;

import dev.rosewood.menuexamples.gui.model.Title;
import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.rosegarden.gui.fill.Fill;
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
import dev.rosewood.rosegarden.gui.provider.slot.SingleSlotProvider;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TitleAreaFill implements Fill {

    public static final String ID = "title-area";
    private int startSlot;
    private int index;

    @Override
    public AbstractItemProvider getItem(Context context, int slot) {
        // getItem runs for every slot in the fill, every time the menu is refreshed, so we need to reset the index
        // when we reach the start slot for another time.
        if (slot == this.startSlot)
            this.index = 0;

        // Both the icon and filtered titles are required for this fill to work
        // So we return an empty item if either of them are missing.
        Optional<Icon> icon = context.get(Parameters.ICON);
        Optional<List<Title>> filteredTitles = context.get(GuiManager.FILTERED_TITLES);
        if (icon.isEmpty() || filteredTitles.isEmpty())
            return new ItemProvider(RoseItem.empty());

        // The item provider is also required, so we return an empty item if it's missing.
        // We grab the provider directly from the icon as then it can be configured.
        Optional<AbstractItemProvider> itemProvider = icon.get().getProvider(Providers.ITEM);
        if (itemProvider.isEmpty())
            return new ItemProvider(RoseItem.empty());

        // Grabs the current title from the filtered list, based on the index,
        // then increases the index for the next time this is called.
        Title title = filteredTitles.get().get(this.index);
        this.index++;

        // We can't directly edit an item provider, as the get method may edit the item itself, so our changes
        // may not be reflected.
        // Instead, we store the data in placeholders so that they can be updated later.
        Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
        StringPlaceholders.Builder updatedPlaceholders = StringPlaceholders.builder();
        placeholders.ifPresent(updatedPlaceholders::addAll);

        updatedPlaceholders.add("title", title.title())
                .add("type", title.type()).build();

        context.add(Parameters.PLACEHOLDERS, updatedPlaceholders.build());

        return itemProvider.get();
    }

    @Override
    public AbstractSlotProvider getSlots(Context context) {
        // The slots however, are called once.

        // Requires the icon and filtered titles too, so just return slot 0 if either are missing.
        Optional<Icon> icon = context.get(Parameters.ICON);
        Optional<List<Title>> filteredTitles = context.get(GuiManager.FILTERED_TITLES);
        if (icon.isEmpty() || filteredTitles.isEmpty()) {
            this.startSlot = 0;
            return SingleSlotProvider.of(0);
        }

        Optional<AbstractSlotProvider> slotProvider = icon.get().getProvider(Providers.SLOT);
        List<Integer> slots;

        // If the icon doesn't have a slot provider, we can use default slots, as no slots were configured.
        if (slotProvider.isEmpty())
            slots = MultiSlotProvider.of("11-15", "20-24", "29-33", "38-42").get(context);
        else
            slots = slotProvider.get().get(context);

        // Then we need to sort them so that we can get the first slot.
        // This is used above to reset the index when we reach the start slot.
        Collections.sort(slots);
        this.startSlot = slots.get(0);

        // As we're filtering the titles, we need to update the slots to match the amount of titles.
        // We shouldn't have 20 slots and only 10 titles, how does that make any sense!!
        MultiSlotProvider updatedSlots = new MultiSlotProvider();
        for (int i = 0; i < filteredTitles.get().size(); i++) {
            updatedSlots.add(slots.get(i));
        }

        return updatedSlots;
    }

    @Override
    public void write(ConfigurationSection section) {
        // Writes "fill: title-area" to the config file, and gets automatically deserialized.
        section.set(FillProvider.ID, ID);
    }

}
