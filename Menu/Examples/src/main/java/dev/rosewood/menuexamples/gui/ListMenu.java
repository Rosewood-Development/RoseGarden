package dev.rosewood.menuexamples.gui;

import dev.rosewood.menuexamples.gui.action.ChangeFilterAction;
import dev.rosewood.menuexamples.gui.fill.TitleAreaFill;
import dev.rosewood.menuexamples.gui.item.FilterItemProvider;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.slot.MultiSlotProvider;
import org.bukkit.Material;

public class ListMenu extends RoseMenuWrapper {

    // The actual menu!

    public static final String ID = "list-example";

    public ListMenu(RosePlugin rosePlugin) {
        super(rosePlugin, ID);
    }

    @Override
    public void create() {
        this.addPage("&3List Example", 54, (page) -> {

            // Fills with a custom class, "TitleAreaFill", also passes the item and slots as they can be configured via the file.
            page.fill(new TitleAreaFill(),
                    RoseItem.of(Material.NAME_TAG, "%title%")
                            .setLore("%type%", "", "&aClick to select"),
                    MultiSlotProvider.of("11-15", "20-24", "29-33", "38-42"));

            // Adds the filter button, uses a custom class "FilterItemProvider" as it has custom settings - the two colour codes passed in.
            // Also uses a custom action, "ChangeFilterAction", which is a simple action that changes the filter.
            page.addIcon(49, new FilterItemProvider(RoseItem.of(Material.SPRUCE_SIGN, "&c&lFilter"), "&a", "&7"))
                   .on(Providers.LEFT_CLICK, new ChangeFilterAction());
        }, 10);
    }

}
