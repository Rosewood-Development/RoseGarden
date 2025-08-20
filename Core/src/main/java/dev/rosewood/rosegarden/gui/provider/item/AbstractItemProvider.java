package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractItemProvider extends AbstractProvider<RoseItem> {

    private final String key;

    public AbstractItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.key = key;
    }

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public String getKey() {
        return this.key;
    }

}
