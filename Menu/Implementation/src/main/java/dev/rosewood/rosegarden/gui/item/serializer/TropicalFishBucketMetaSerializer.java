package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.TropicalFish;

/**
 * item:
 * type: tropical_fish_bucket
 * tropical-fish-bucket:
 * pattern-color: red
 * body-color: white
 * pattern: kob
 */

public class TropicalFishBucketMetaSerializer implements MetaSerializer {

    public static final String TROPICAL_FISH_BUCKET = "tropical-fish-bucket";
    public static final String PATTERN_COLOR = TROPICAL_FISH_BUCKET + ".pattern-color";
    public static final String BODY_COLOR = TROPICAL_FISH_BUCKET + ".body-color";
    public static final String PATTERN = TROPICAL_FISH_BUCKET + ".pattern";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(TROPICAL_FISH_BUCKET))
            return;

        if (section.contains(PATTERN) && section.isString(PATTERN)) {
            TropicalFish.Pattern pattern = this.getPattern(section.getString(PATTERN));
            if (pattern != null)
                item.setTropicalFishPattern(pattern);
        }

        if (section.contains(PATTERN_COLOR) && section.isString(PATTERN_COLOR)) {
            DyeColor color = ItemSerializer.getDyeColor(section.getString(PATTERN_COLOR), TROPICAL_FISH_BUCKET);
            if (color != null)
                item.setTropicalFishPatternColor(color);
        }

        if (section.contains(BODY_COLOR) && section.isString(BODY_COLOR)) {
            DyeColor color = ItemSerializer.getDyeColor(section.getString(BODY_COLOR), TROPICAL_FISH_BUCKET);
            if (color != null)
                item.setTropicalFishBodyColor(color);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasTropicalFishVariant())
            return;

        if (item.getTropicalFishPattern() != null)
            section.set(PATTERN, item.getTropicalFishPattern().toString().toLowerCase());

        if (item.getTropicalFishPatternColor() != null)
            section.set(PATTERN_COLOR, item.getTropicalFishPatternColor().toString().toLowerCase());

        if (item.getTropicalFishBodyColor() != null)
            section.set(BODY_COLOR, item.getTropicalFishBodyColor().toString().toLowerCase());
    }

    private TropicalFish.Pattern getPattern(String str) {
        try {
            return TropicalFish.Pattern.valueOf(str);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid tropical fish pattern: '" + str + "' for " + TROPICAL_FISH_BUCKET + " item meta!");
            return null;
        }
    }

}
