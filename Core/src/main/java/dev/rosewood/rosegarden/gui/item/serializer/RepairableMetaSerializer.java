package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.configuration.ConfigurationSection;

/**
 * item:
 *   type: iron_sword
 *   repairable:
 *     repair-cost: 4
 */
public class RepairableMetaSerializer implements MetaSerializer {

    public static final String REPAIRABLE = "repairable";
    public static final String REPAIR_COST = REPAIRABLE + ".repair-cost";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(REPAIRABLE) || !section.contains(REPAIR_COST))
            return;

        if (NMSUtil.getVersionNumber() < 18) {
            this.invalidateVersion(REPAIRABLE, "1.16");
        }

        if (!section.isInt(REPAIR_COST))
            return;

        item.setRepairCost(section.getInt(REPAIR_COST));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasRepairCost())
            return;

        section.set(REPAIR_COST, item.getRepairCost());
    }

}
