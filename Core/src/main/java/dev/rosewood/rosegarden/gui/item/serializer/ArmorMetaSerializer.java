package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

/**
 * item:
 *   type: netherite_chestplate
 *   armor:
 *     trim-material: gold
 *     trim-pattern: sentry
 */
@SuppressWarnings("deprecation")
public class ArmorMetaSerializer implements MetaSerializer {

    public static final String ARMOR = "armor";
    public static final String TRIM_MATERIAL = ARMOR + ".trim-material";
    public static final String TRIM_PATTERN = ARMOR + ".trim-pattern";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(ARMOR) || !section.contains(TRIM_MATERIAL) || !section.contains(TRIM_PATTERN))
            return;

        if (NMSUtil.getVersionNumber() < 19) {
            this.invalidateVersion(ARMOR, "1.19");
            return;
        }

        if (!section.isString(TRIM_PATTERN) || !section.isString(TRIM_PATTERN))
            return;

        TrimMaterial material = this.getMaterial(section.getString(TRIM_MATERIAL));
        if (material == null)
            return;

        TrimPattern pattern = this.getPattern(section.getString(TRIM_PATTERN));
        if (pattern == null)
            return;

        item.setArmorTrim(new ArmorTrim(material, pattern));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasArmorTrim())
            return;

        ArmorTrim trim = item.getArmorTrim();
        section.set(TRIM_MATERIAL, trim.getMaterial().getKey().toString());
        section.set(TRIM_PATTERN, trim.getPattern().getKey().toString());
    }

    private TrimMaterial getMaterial(String str) {
        NamespacedKey key = ItemSerializer.getKey(str, ARMOR);
        if (key == null)
            return null;

        TrimMaterial material = Registry.TRIM_MATERIAL.get(key);
        if (material == null) {
            Bukkit.getLogger().warning("Invalid trim material: '" + str + "' for " + ARMOR + " item meta!");
            return null;
        }

        return material;
    }

    private TrimPattern getPattern(String str) {
        NamespacedKey key = ItemSerializer.getKey(str, ARMOR);
        if (key == null)
            return null;

        TrimPattern pattern = Registry.TRIM_PATTERN.get(key);
        if (pattern == null) {
            Bukkit.getLogger().warning("Invalid trim pattern: '" + str + "' for " + ARMOR + " item meta!");
            return null;
        }

        return pattern;
    }

}
