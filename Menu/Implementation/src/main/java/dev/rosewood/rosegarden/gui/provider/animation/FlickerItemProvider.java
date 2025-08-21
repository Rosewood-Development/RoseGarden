package dev.rosewood.rosegarden.gui.provider.animation;

import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.CompositeItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Optional;

public class FlickerItemProvider extends AbstractItemProvider implements Tickable {

    public static final String ID = "flicker";

    private final AbstractItemProvider initialItem;
    private final AbstractItemProvider flickerItem;
    private AbstractItemProvider displayItem;
    private boolean displayInitial;

    public FlickerItemProvider(Item initialItem, Item flickerItem) {
        super(ID, null);

        this.initialItem = new ItemProvider(initialItem);
        this.flickerItem = new ItemProvider(flickerItem);
        this.displayItem = this.initialItem;
        this.displayInitial = true;
    }

    public FlickerItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.initialItem = new CompositeItemProvider(key + ".to", section);
        this.flickerItem = new CompositeItemProvider(key + ".from", section);
        this.displayItem = this.initialItem;
        this.displayInitial = true;
    }

    @Override
    public void write(ConfigurationSection section) {
        this.initialItem.write(section.createSection(this.getKey() + ".to"));
        this.flickerItem.write(section.createSection(this.getKey() + ".from"));
    }

    @Override
    public RoseItem get(Context context) {
        Optional<RoseItem> item = context.get(Parameters.ITEM);
        return this.displayItem.get(context).mergeWith(item.orElse(RoseItem.empty()));
    }

    @Override
    public void run(Context context) {
        this.displayItem = this.displayInitial ? this.initialItem : this.flickerItem;
        this.displayInitial = !this.displayInitial;
    }

}
