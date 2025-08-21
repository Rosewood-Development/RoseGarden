package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.components.ToolComponent;

/**
 * item:
 *   type: iron_pickaxe
 *   tool:
 *     mining-speed: 4
 *     damage-per-block: 2
 *     rules:
 *       stone:
 *         speed: 3
 *         correct-for-drops: true
 */
public class ToolComponentSerializer implements MetaSerializer {

    public static final String TOOL = "tool";
    public static final String MINING_SPEED = TOOL + ".mining-speed";
    public static final String DAMAGE_PER_BLOCK = TOOL + ".damage-per-block";
    public static final String RULES = TOOL + ".rules";
    public static final String SPEED = "speed";
    public static final String CORRECT_FOR_DROPS = "correct-for-drops";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(TOOL))
            return;

        if (section.contains(MINING_SPEED) && section.isDouble(MINING_SPEED))
            item.setToolMiningSpeed((float) section.getDouble(MINING_SPEED));

        if (section.contains(DAMAGE_PER_BLOCK) && section.isInt(DAMAGE_PER_BLOCK))
            item.setToolDamagePerBlock(section.getInt(DAMAGE_PER_BLOCK));

        if (!section.contains(RULES) || !section.isConfigurationSection(RULES))
            return;

        ConfigurationSection rulesSection = section.getConfigurationSection(RULES);
        if (rulesSection == null)
            return;

        for (String blockStr : rulesSection.getKeys(false)) {
            Material block = ItemSerializer.getMaterial(blockStr);
            if (block == null)
                continue;

            Float speed = null;
            Boolean correctForDrops = null;

            String key = RULES + "." + blockStr + ".";
            if (section.contains(key + SPEED) && section.isDouble(key + SPEED))
                speed = (float) section.getDouble(key + SPEED);

            if (section.contains(key + CORRECT_FOR_DROPS) && section.isInt(key = CORRECT_FOR_DROPS))
                correctForDrops = section.getBoolean(key + CORRECT_FOR_DROPS);

            item.addToolRule(block, speed, correctForDrops);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasToolComponent())
            return;

        if (item.getToolMiningSpeed() != null)
            section.set(MINING_SPEED, item.getToolMiningSpeed());

        if (item.getToolDamagePerBlock() != null)
            section.set(DAMAGE_PER_BLOCK, item.getToolDamagePerBlock());

        for (Object obj : item.getToolRules()) {
            ToolComponent.ToolRule rule = (ToolComponent.ToolRule) obj;

            for (Material material : rule.getBlocks()) {
                if (rule.getSpeed() != null)
                    section.set(RULES + "." + material.toString().toLowerCase() + "." + SPEED, rule.getSpeed());

                if (rule.isCorrectForDrops() != null && rule.isCorrectForDrops())
                    section.set(RULES + "." + material.toString().toLowerCase() + "." + CORRECT_FOR_DROPS, true);
            }
        }
    }

}
