package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageType;
import org.bukkit.tag.DamageTypeTags;

@SuppressWarnings("UnstableApiUsage")
public class DamageResistantSerializer implements MetaSerializer {

    public static final String DAMAGE_RESISTANT = "damage-resistant";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(DAMAGE_RESISTANT))
            return;

        if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
            Bukkit.getLogger().warning("The " + DAMAGE_RESISTANT + " item meta is only available on 1.21.3+!");
            return;
        }

        NamespacedKey key = ItemSerializer.getKey(section.getString(DAMAGE_RESISTANT), DAMAGE_RESISTANT);
        if (key == null)
            return;

        Tag<DamageType> tag = Bukkit.getTag(DamageTypeTags.REGISTRY_DAMAGE_TYPES, key, DamageType.class);
        item.setDamageResistant(tag);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasDamageResistant())
            return;

        Tag<DamageType> tag = item.getDamageResistant();
        section.set(DAMAGE_RESISTANT, tag.getKey().toString());
    }

}
