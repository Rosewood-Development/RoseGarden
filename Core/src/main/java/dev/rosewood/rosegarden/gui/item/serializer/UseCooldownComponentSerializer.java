package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: egg
 *   use-cooldown:
 *     time: 2
 *     group: minecraft:group
 */
public class UseCooldownComponentSerializer implements MetaSerializer {

    public static final String USE_COOLDOWN = "use-cooldown";
    public static final String TIME = USE_COOLDOWN + ".time";
    public static final String GROUP = USE_COOLDOWN + ".group";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(USE_COOLDOWN))
            return;

        if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
            this.invalidateVersion(USE_COOLDOWN, "1.21.3");
            return;
        }

        if (section.contains(TIME) && section.isInt(TIME))
            item.setUseCooldownSeconds((float) section.getDouble(TIME));

        if (section.contains(GROUP) && section.isString(GROUP)) {
            NamespacedKey key = ItemSerializer.getKey(section.getString(GROUP), USE_COOLDOWN);
            if (key != null)
                item.setUseCooldownGroup(key);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasUseCooldown())
            return;

        if (item.getUseCooldownSeconds() != 0.0F)
            section.set(TIME, item.getUseCooldownSeconds());

        if (item.getUseCooldownGroup() != null)
            section.set(GROUP, item.getUseCooldownGroup());
    }

}
