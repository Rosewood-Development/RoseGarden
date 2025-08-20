package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

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
            this.item = (context) -> RoseItem.deserialize(section.getConfigurationSection(key));
    }

    @Override
    public void write(ConfigurationSection section) {
        RoseItem item = this.item.get(Context.empty());
        if (item.isEmpty())
            return;

        item.serialize(section.createSection(this.getKey()));
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
