package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.provider.Providers;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a configured item that can be {@linkplain RoseItem#mergeWith(RoseItem) merged} with another item.<br>
 * A regular {@linkplain ItemProvider item provider} can be used in code and is merged automatically.<br>
 * This allows for item definitions to contain only properties that should be changed.<br>
 * This is used by default in configs.<br>
 * Usage:<br>
 * <pre>
 *     {@code
 *          item:
 *            type: conditional
 *            conditions:
 *              '0':
 *                conditions:
 *                  - '%player_has_permission_test.perm%'
 *                  true:
 *                    item:
 *                      type: item
 *                      item: diamond
 *                  false:
 *                    item:
 *                      type: item
 *                      item: coal
 *     }
 * </pre>
 * This creates diamond with a different name depending on the condition.<br>
 * See {@linkplain dev.rosewood.rosegarden.gui.item.ItemSerializer ItemSerializer} for meta serializers.
 */
public class CompositeItemProvider extends AbstractItemProvider {

    private final AbstractItemProvider initialItem;
    private final List<AbstractItemProvider> providers;

    // Config Constructors

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

            String type = itemSection.getString("type");
            if (type == null)
                return;

            if (type.equals(providerType.getKey())) {
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
