package dev.rosewood.rosegarden.gui.provider.slot;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSlotProvider extends AbstractProvider<List<Integer>> {

    private final String key;
    protected final List<Integer> slots;

    public AbstractSlotProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.key = key;
        this.slots = new ArrayList<>();
    }

    @Override
    public List<Integer> get(Context context) {
        return this.slots;
    }

    @Override
    public String getType() {
        return "slot";
    }

    @Override
    public String getKey() {
        return this.key;
    }

}
