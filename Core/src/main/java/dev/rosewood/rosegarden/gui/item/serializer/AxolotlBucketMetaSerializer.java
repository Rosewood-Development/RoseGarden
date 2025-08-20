package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Axolotl;

/**
 * item:
 *   type: axolotl_bucket
 *   axolotl-bucket:
 *     variant: lucy
 */
public class AxolotlBucketMetaSerializer implements MetaSerializer {

    public static final String AXOLOTL_BUCKET = "axolotl-bucket";
    public static final String VARIANT = AXOLOTL_BUCKET + ".variant";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(AXOLOTL_BUCKET) || !section.contains(VARIANT))
            return;

        if (NMSUtil.getVersionNumber() < 17) {
            this.invalidateVersion(AXOLOTL_BUCKET, "1.17");
            return;
        }

        if (!section.isString(VARIANT))
            return;

        Axolotl.Variant variant = this.getVariant(section.getString(VARIANT));
        if (variant == null)
            return;

        item.setAxolotlVariant(variant);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasAxolotlVariant())
            return;

        section.set(VARIANT, item.getAxolotlVariant().toString().toLowerCase());
    }

    private Axolotl.Variant getVariant(String str) {
        try {
            return Axolotl.Variant.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid axolotl variant: '" + str + "' for " + AXOLOTL_BUCKET + " item meta!");
            return null;
        }
    }

}
