package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents an item in an inventory.<br>
 * Serializes as:<br>
 * <pre>
 *     {@code
 *     item:
 *       type: stone
 *     }
 * </pre>
 * See {@linkplain dev.rosewood.rosegarden.gui.item.ItemSerializer ItemSerializer} for meta serializers.
 */
public class ItemProvider extends AbstractItemProvider {

    public static final String ID = "item";

    protected Item item;

    // Code Constructors

    public ItemProvider(Item item) {
        super(ID, null);

        this.item = item;
    }

    // Config Constructors

    public ItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        if (section.contains(key))
            this.item = (context) -> RoseItem.deserialize(section.getConfigurationSection("item"));
    }

    @Override
    public void write(ConfigurationSection section) {
        super.write(section);
        RoseItem item = this.item.get(Context.empty());
        item.serialize(section.getConfigurationSection("item"));
    }

    @Override
    public RoseItem get(Context context) {
        return this.item.get(context);
    }

    // Static Constructors

    public static ItemProvider of(Item item) {
        return new ItemProvider(item);
    }

}
