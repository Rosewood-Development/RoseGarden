package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

@SuppressWarnings("deprecation")
public class EnchantmentStorageMetaSerializer implements MetaSerializer {

    public static final String STORED_ENCHANTMENTS = "stored-enchantments";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(STORED_ENCHANTMENTS))
            return;

        if (section.isConfigurationSection(STORED_ENCHANTMENTS)) {
            ConfigurationSection enchantmentsSection = section.getConfigurationSection(STORED_ENCHANTMENTS);
            if (enchantmentsSection != null) {
                for (String enchantmentStr : enchantmentsSection.getKeys(false)) {
                    NamespacedKey key = ItemSerializer.getKey(enchantmentStr, STORED_ENCHANTMENTS);
                    if (key == null)
                        continue;

                    Enchantment enchantment = Enchantment.getByKey(key);
                    int level = enchantmentsSection.getInt(enchantmentStr);

                    item.addStoredEnchantment(enchantment, level);
                }
            }
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasStoredEnchantments())
            return;

        for (Enchantment enchantment : item.getStoredEnchantments().keySet()) {
            String key = enchantment.getKey().toString();
            int level = item.getStoredEnchantmentLevel(enchantment);

            section.set(STORED_ENCHANTMENTS + "." + key, level);
        }
    }

}
