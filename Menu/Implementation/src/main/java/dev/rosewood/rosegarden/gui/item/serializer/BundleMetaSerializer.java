package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * item:
 *   type: bundle
 *   bundle:
 *     items:
 *       1:
 *         type: stone
 *       2:
 *         type: stick
 */
public class BundleMetaSerializer implements MetaSerializer {

    public static final String BUNDLE = "bundle";
    public static final String ITEMS = BUNDLE + ".items";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(BUNDLE) || !section.contains(ITEMS))
            return;

        if (NMSUtil.getVersionNumber() < 17) {
            this.invalidateVersion(BUNDLE, "1.17");
            return;
        }

        if (!section.isConfigurationSection(ITEMS))
            return;

        ConfigurationSection itemsSection = section.getConfigurationSection(ITEMS);
        if (itemsSection == null)
            return;

        for (String id : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(ITEMS + "." + id);
            if (itemSection == null)
                continue;

            RoseItem bundleItem = ItemSerializer.deserialize(itemSection);
            item.addBundleItem(bundleItem.asItemStack());
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasBundleItems())
            return;

        int index = 0;
        for (ItemStack itemStack : item.getBundleItems()) {
            index++;

            ConfigurationSection itemSection = section.createSection(ITEMS + "." + index);
            ItemSerializer.serialize(new RoseItem(itemStack), itemSection);
        }
    }

}
