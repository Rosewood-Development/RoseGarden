package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class CrossbowMetaSerializer implements MetaSerializer {

    public static final String CROSSBOW = "crossbow";
    public static final String PROJECTILES = CROSSBOW + ".projectiles";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(CROSSBOW))
            return;

        ConfigurationSection projectilesSection = section.getConfigurationSection(PROJECTILES);
        if (projectilesSection == null)
            return;

        for (String index : projectilesSection.getKeys(false)) {
            ConfigurationSection indexSection = projectilesSection.getConfigurationSection(index);
            if (indexSection == null)
                continue;

            RoseItem projectile = ItemSerializer.deserialize(indexSection);
            if (projectile != null)
                item.addCrossbowProjectile(projectile.asItemStack());
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasCrossbowProjectiles())
            return;

        int index = 0;
        for (ItemStack projectile : item.getCrossbowProjectiles()) {
            index++;
            RoseItem roseProjectile = new RoseItem(projectile);

            ItemSerializer.serialize(roseProjectile, section.createSection(PROJECTILES + "." + index));
        }
    }

}
