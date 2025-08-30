package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractItemProvider extends AbstractProvider<RoseItem> {

    public static final String ITEM = "item";

    private final String key;

    public AbstractItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.key = key;
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(ITEM + ".type", this.key);
    }

    @Override
    public String getType() {
        return ITEM;
    }

    @Override
    public String getKey() {
        return this.key;
    }

}
