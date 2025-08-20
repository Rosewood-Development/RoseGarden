package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: ominous_bottle
 *   ominous-bottle:
 *     amplifier: 1
 */
public class OminousBottleMetaSerializer implements MetaSerializer {

    public static final String OMINOUS_BOTTLE = "ominous-bottle";
    public static final String AMPLIFIER = OMINOUS_BOTTLE + ".amplifier";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(OMINOUS_BOTTLE) || !section.contains(AMPLIFIER))
            return;

        if (NMSUtil.getVersionNumber() < 21) {
            this.invalidateVersion(OMINOUS_BOTTLE, "1.21");
            return;
        }

        if (!section.isInt(AMPLIFIER))
            return;

        item.setOminousBottleAmplifier(section.getInt(AMPLIFIER));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasOminousBottleAmplifier())
            return;

        section.set(AMPLIFIER, item.getOminousBottleAmplifier());
    }

}
