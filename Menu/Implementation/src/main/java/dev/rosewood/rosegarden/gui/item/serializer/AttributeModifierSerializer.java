package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Collection;

/**
 * item:
 *   type: iron_sword
 *   attributes:
 *     minecraft:generic_movement_speed:
 *       amount: 20
 *       operation: add_value
 */
@SuppressWarnings({"removal"})
public class AttributeModifierSerializer implements MetaSerializer {

    public static final String ATTRIBUTES = "attributes";
    public static final String AMOUNT = "amount";
    public static final String OPERATION = "operation";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(ATTRIBUTES) || !section.isConfigurationSection(ATTRIBUTES))
            return;

        ConfigurationSection attributeSection = section.getConfigurationSection(ATTRIBUTES);
        if (attributeSection == null)
            return;

        for (String attributeStr : attributeSection.getKeys(false)) {
            Attribute attribute = this.getAttribute(attributeStr, ATTRIBUTES);
            if (attribute == null)
                continue;

            double amount = 0.0D;
            if (attributeSection.contains(attributeStr + "." + AMOUNT))
                amount = attributeSection.getDouble(attributeStr + "." + AMOUNT);

            AttributeModifier.Operation operation = AttributeModifier.Operation.ADD_NUMBER;
            if (attributeSection.contains(attributeStr + "." + OPERATION))
                operation = this.getOperation(attributeSection.getString(attributeStr + "." + OPERATION), ATTRIBUTES);

            if (operation == null)
                return;

            AttributeModifier modifier = new AttributeModifier(attribute.getKey().toString(), amount, operation);
            item.addAttributeModifier(attribute, modifier);
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasAttributeModifiers())
            return;

        for (Attribute attribute : item.getAttributeModifiers().keys()) {
            Collection<AttributeModifier> modifiers = item.getAttributeModifiers().get(attribute);
            if (modifiers.isEmpty())
                continue;

            for (AttributeModifier modifier : modifiers) {
                String key = ATTRIBUTES + "." + attribute.getKey();
                section.set(key + "." + AMOUNT, modifier.getAmount());
                section.set(key + "." + OPERATION, modifier.getOperation().toString().toLowerCase());
                return;
            }
        }
    }

    private Attribute getAttribute(String attributeStr, String meta) {
        try {
            return Attribute.valueOf(attributeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid attribute: '" + attributeStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    private AttributeModifier.Operation getOperation(String operationStr, String meta) {
        try {
            return AttributeModifier.Operation.valueOf(operationStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid operation: '" + operationStr + "' for " + meta + " item meta!");
            return null;
        }
    }

}
