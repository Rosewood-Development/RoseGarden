package dev.rosewood.menuexamples.gui.item;

import dev.rosewood.menuexamples.gui.model.Filter;
import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Optional;

// Custom item provider!
public class FilterItemProvider extends AbstractItemProvider {

    public static final String ID = "filter-item";

    protected Item item;

    // Has config options for selected and deselected colours.
    private final String selectedColor;
    private final String deselectedColor;

    // Code Constructors

    public FilterItemProvider(Item item, String selectedColor, String deselectedColor) {
        super(ID, null);

        this.item = item;
        this.selectedColor = selectedColor;
        this.deselectedColor = deselectedColor;
    }

    public FilterItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        // Grabs the item and colours from the config.
        if (section.contains(ID))
            this.item = (context) -> RoseItem.deserialize(section.getConfigurationSection(ID));

        this.selectedColor = section.contains(ID + ".selected-color") ? section.getString(ID + ".selected-color") : "";
        this.deselectedColor = section.contains(ID + ".deselected-color") ? section.getString(ID  + ".deselected-color") : "";
    }

    @Override
    public void write(ConfigurationSection section) {
        RoseItem item = this.item.get(Context.empty());
        if (item.isEmpty())
            return;

        // Writes the item and colours to the config.
        item.serialize(section.createSection(ID));

        section.set(ID + ".selected-color", this.selectedColor);
        section.set(ID + ".deselected-color", this.deselectedColor);
    }

    @Override
    public RoseItem get(Context context) {
        // If the filter is present (should always be, but we check just in case!)
        Optional<Filter> filterOpt = context.get(GuiManager.FILTER);
        if (filterOpt.isPresent()) {
            Filter filter = filterOpt.get();

            // Sets the colour if the filter is selected or deselected.
            String none = filter == Filter.NONE ? this.selectedColor : this.deselectedColor;
            String name = filter == Filter.NAME ? this.selectedColor : this.deselectedColor;
            String type = filter == Filter.TYPE ? this.selectedColor : this.deselectedColor;
            return this.item.get(context)
                    .setLore("&f- " + none + "None",
                            "&f- " + name + "By Name",
                            "&f- " + type + "By Type");
        }

        return this.item.get(context)
                .setLore("&f- " + this.selectedColor + "None",
                        "&f- " + this.deselectedColor + "By Name",
                        "&f- " + this.deselectedColor + "By Type");
    }

}
