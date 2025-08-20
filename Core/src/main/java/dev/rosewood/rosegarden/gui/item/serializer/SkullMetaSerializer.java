package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: player_head
 *   skull:
 *     owner: %player_name%
 *     sound: minecraft:block_noteblock_pling
 *     texture: eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWExZjM1ODVmNTQ4NDliNDFkYTVkMzI2YTk0Mzk1NmEyNTQ5NDM2NGJiOTYxZDI0NThjNDMwYmU5YTZiMjcifX19
 */

public class SkullMetaSerializer implements MetaSerializer {

    public static final String SKULL = "skull";
    public static final String OWNER = SKULL + ".owner";
    public static final String SOUND = SKULL + ".sound";
    public static final String TEXTURE = SKULL + ".texture";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(SKULL))
            return;

        if (section.contains(OWNER) && section.isString(OWNER))
            item.setSkullOwner(section.getString(OWNER));

        if (section.contains(TEXTURE) && section.isString(TEXTURE)) {
            if (NMSUtil.getVersionNumber() < 18) {
                this.invalidateVersion(SKULL, "1.18");
                return;
            }

            item.setSkullTexture(section.getString(TEXTURE));
        }

        if (section.contains(SOUND) && section.isString(SOUND)) {
            if (NMSUtil.getVersionNumber() < 19) {
                this.invalidateVersion(SKULL, "1.19");
                return;
            }

            NamespacedKey key = ItemSerializer.getKey(section.getString(SOUND), SKULL);
            if (key == null)
                return;

            item.setSkullSound(key);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.hasSkullOwner())
            section.set(OWNER, item.getSkullOwner());

        if (item.getSkullSound() != null)
            section.set(SOUND, item.getSkullSound().toString());

        if (item.getSkullTexture() != null)
            section.set(TEXTURE, item.getSkullTexture());
    }

}
