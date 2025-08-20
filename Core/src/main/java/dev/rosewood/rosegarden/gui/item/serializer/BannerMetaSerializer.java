package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: white_banner
 *   banner-patterns:
 *     1:
 *       type: cross
 *       color: red
 */
public class BannerMetaSerializer implements MetaSerializer {

    public static final String BANNER_PATTERNS = "banner-patterns";
    public static final String TYPE = "type";
    public static final String COLOR = "color";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(BANNER_PATTERNS))
            return;

        if (!section.isConfigurationSection(BANNER_PATTERNS))
            return;

        ConfigurationSection patternSection = section.getConfigurationSection(BANNER_PATTERNS);
        if (patternSection == null)
            return;

        for (String id : patternSection.getKeys(false)) {
            PatternType type = this.getPatternType(patternSection.getString(id + "." + TYPE), BANNER_PATTERNS);
            if (type == null)
                continue;

            DyeColor color = ItemSerializer.getDyeColor(patternSection.getString(id + "." + COLOR), BANNER_PATTERNS);
            if (color == null)
                continue;

            item.addBannerPattern(new Pattern(color, type));
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.getBannerPatterns() == null || item.getBannerPatterns().isEmpty())
            return;

        int index = 0;
        for (Pattern pattern : item.getBannerPatterns()) {
            index++;

            PatternType type = pattern.getPattern();
            DyeColor color = pattern.getColor();
            String key = BANNER_PATTERNS + "." + index + ".";
            section.set(key + TYPE, type.toString().toLowerCase());
            section.set(key + COLOR, color.toString().toLowerCase());
        }
    }

    public PatternType getPatternType(String typeStr, String meta) {
        try {
            return PatternType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid pattern type: '" + typeStr + "' for " + meta + " item meta!");
            return null;
        }
    }

}
