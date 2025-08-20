package dev.rosewood.rosegarden.gui.provider.fill;

import dev.rosewood.rosegarden.gui.fill.Fill;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractFillProvider extends AbstractProvider<Fill> {

    private final String key;

    public AbstractFillProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.key = key;
    }

    @Override
    public String getType() {
        return "fill";
    }

    @Override
    public String getKey() {
        return this.key;
    }



}
