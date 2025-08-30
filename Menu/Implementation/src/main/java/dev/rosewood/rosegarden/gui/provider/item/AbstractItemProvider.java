package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import dev.rosewood.rosegarden.gui.provider.Provider;
import dev.rosewood.rosegarden.gui.provider.Providers;
import org.bukkit.configuration.ConfigurationSection;

public abstract class AbstractItemProvider extends AbstractProvider<RoseItem> {

    public static final String ITEM = "item";

    private final String id;

    public AbstractItemProvider(String id, ConfigurationSection section) {
        super(id, section);

        this.id = id;
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(ITEM + ".type", this.id);
    }

    @Override
    public String getGroup() {
        return ITEM;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public static AbstractItemProvider deserialize(ConfigurationSection section) {
        if (section == null || !section.contains("type"))
            return null;

        String type = section.getString("type");
        for (Providers.ProviderType<?> providerType : Providers.getRegistry().values()) {
            if (!providerType.getId().equalsIgnoreCase(type))
                continue;

            return (AbstractItemProvider) providerType.create(section);
        }

        return null;
    }

}
