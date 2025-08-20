package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.Providers;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class CompositeItemProvider extends AbstractItemProvider {

    private final AbstractItemProvider initialItem;
    private final List<AbstractItemProvider> providers;

    public CompositeItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.initialItem = new ItemProvider(key, section);
        this.providers = new ArrayList<>();

        ConfigurationSection itemSection = section.getConfigurationSection(key);
        if (itemSection == null)
            return;

        for (Providers.ProviderType<?> providerType : Providers.getRegistry().values()) {
            if (providerType.getKey().equalsIgnoreCase(this.getKey()) || !providerType.getType().equalsIgnoreCase(Providers.ITEM.getType()))
                continue;

            if (itemSection.contains(providerType.getKey())) {
                AbstractItemProvider provider = (AbstractItemProvider) providerType.create(itemSection);
                this.providers.add(provider);
            }
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        // No serialization
    }

    @Override
    public RoseItem get(Context context) {
        RoseItem output = this.initialItem.get(context);
        for (AbstractItemProvider provider : this.providers)
            output = output.mergeWith(provider.get(context));

        return output;
    }

}
