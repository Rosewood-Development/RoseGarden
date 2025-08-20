package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * item:
 *   type: netherite_chestplate
 *   armor:
 *     trim-material: gold
 *     trim-pattern: sentry
 */
public class LeatherArmorMetaSerializer implements MetaSerializer {

    public static final String LEATHER_ARMOR = "leather-armor";
    public static final String COLOR = LEATHER_ARMOR + ".color";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(LEATHER_ARMOR) || !section.contains(COLOR))
            return;

        if (section.isString(COLOR)) {
            String str = section.getString(COLOR);
            if (str == null)
                return;

            if (!str.startsWith("#"))
                return;

            Color color = ItemSerializer.getColor(str);
            if (color != null)
                item.setColor(color);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasColor() || !(item.getMeta() instanceof LeatherArmorMeta))
            return;

        Color color = item.getColor();
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        String hex = String.format("#%02X%02X%02X", r, g, b);

        section.set(COLOR, hex);
    }

}
