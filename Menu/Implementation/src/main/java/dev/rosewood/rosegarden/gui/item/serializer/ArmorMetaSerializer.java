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
 * <pre>
 *     {@code
 * item:
 *   type: netherite_chestplate
 *   trim:
 *     material: gold
 *     pattern: sentry
 *     }
 * </pre>
 */
@SuppressWarnings("deprecation")
public class ArmorMetaSerializer implements MetaSerializer {

    public static final String TRIM = "trim";
    public static final String MATERIAL = TRIM + ".material";
    public static final String PATTERN = TRIM + ".pattern";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(TRIM) || !section.contains(MATERIAL) || !section.contains(PATTERN))
            return;

        if (NMSUtil.getVersionNumber() < 19) {
            this.invalidateVersion(TRIM, "1.19");
            return;
        }

        if (!section.isString(PATTERN) || !section.isString(PATTERN))
            return;

        TrimMaterial material = this.getMaterial(section.getString(MATERIAL));
        if (material == null)
            return;

        TrimPattern pattern = this.getPattern(section.getString(PATTERN));
        if (pattern == null)
            return;

        item.setArmorTrim(new ArmorTrim(material, pattern));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasArmorTrim())
            return;

        ArmorTrim trim = item.getArmorTrim();
        section.set(MATERIAL, trim.getMaterial().getKey().toString());
        section.set(PATTERN, trim.getPattern().getKey().toString());
    }

    private TrimMaterial getMaterial(String str) {
        NamespacedKey key = ItemSerializer.getKey(str, TRIM);
        if (key == null)
            return null;

        TrimMaterial material = Registry.TRIM_MATERIAL.get(key);
        if (material == null) {
            Bukkit.getLogger().warning("Invalid trim material: '" + str + "' for " + TRIM + " item meta!");
            return null;
        }

        return material;
    }

    private TrimPattern getPattern(String str) {
        NamespacedKey key = ItemSerializer.getKey(str, TRIM);
        if (key == null)
            return null;

        TrimPattern pattern = Registry.TRIM_PATTERN.get(key);
        if (pattern == null) {
            Bukkit.getLogger().warning("Invalid trim pattern: '" + str + "' for " + TRIM + " item meta!");
            return null;
        }

        return pattern;
    }

}
