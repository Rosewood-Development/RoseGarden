package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.condition.ConditionParser;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents an item with a condition.<br>
 * Serializes as:<br>
 * <pre>
 *     {@code
 *     item:
 *       requires-condition
 *         condition: '%some_condition%'
 *         pass:
 *           item:
 *             type: diamond:
 *         fail:
 *           item:
 *             type: stone
 *     }
 * </pre>
 * See {@linkplain dev.rosewood.rosegarden.gui.item.ItemSerializer ItemSerializer} for meta serializers.
 */
public class ConditionalItemProvider extends AbstractItemProvider {

    public static final String ID = "conditional";

    protected final List<ConditionalItem> conditionalItems;

    // Code Constructors

    public ConditionalItemProvider(ConditionalItem... conditionalItems) {
        super(ID, null);

        this.conditionalItems = List.of(conditionalItems);
    }

    // Config Constructors

    public ConditionalItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.conditionalItems = new ArrayList<>();
        ConfigurationSection conditionsSection = section.getConfigurationSection("conditions");
        if (conditionsSection == null)
            return;

        for (String id : conditionsSection.getKeys(false)) {
            ConfigurationSection idSection = conditionsSection.getConfigurationSection(id);
            if (idSection == null)
                continue;

            List<String> conditions = idSection.contains("conditions") ?
                    idSection.getStringList("conditions") : new ArrayList<>();
            RoseItem rootItem = RoseItem.deserialize(idSection);
            RoseItem trueItem = idSection.contains("true") ?
                    RoseItem.deserialize(idSection.getConfigurationSection("true.item")) : RoseItem.empty();
            RoseItem falseItem = idSection.contains("false") ?
                    RoseItem.deserialize(idSection.getConfigurationSection("false.item")) : RoseItem.empty();

            this.conditionalItems.add(new ConditionalItem(conditions, rootItem, trueItem, falseItem));
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        super.write(section);
        ConfigurationSection conditionsSection = section.createSection("item.conditions");
        for (int i = 0; i < this.conditionalItems.size(); i++) {
            ConditionalItem item = this.conditionalItems.get(i);
            ConfigurationSection idSection = conditionsSection.createSection(String.valueOf(i));
            idSection.set("conditions", item.stringConditions);

            ConfigurationSection trueSection = idSection.createSection("true");
            item.trueItem.write(trueSection);

            ConfigurationSection falseSection = idSection.createSection("false");
            item.falseItem.write(falseSection);
        }
    }

    @Override
    public RoseItem get(Context context) {
        Optional<RoseItem> item = context.get(Parameters.ITEM);
        RoseItem originalItem = item.orElse(RoseItem.empty());

        for (ConditionalItem conditionalItem : this.conditionalItems) {
            originalItem.mergeWith(conditionalItem.rootItem.get(context));

            boolean success = true;
            for (Condition condition : conditionalItem.conditions) {
                if (!condition.check(context)) {
                    success = false;
                    break;
                }
            }

            originalItem.mergeWith(success ? conditionalItem.trueItem.get(context) : conditionalItem.falseItem.get(context));

        }

        return originalItem;
    }

    public ConditionalItemProvider and(Item trueItem, Item falseItem, String... conditions) {
        this.conditionalItems.add(new ConditionalItem(List.of(conditions), RoseItem.empty(), trueItem, falseItem));
        return this;
    }

    // Static Constructors

    public static ConditionalItemProvider of(Item trueItem, Item falseItem, String... conditions) {
        return new ConditionalItemProvider(new ConditionalItem(List.of(conditions), RoseItem.empty(), trueItem, falseItem));
    }

    protected static class ConditionalItem {

        protected final List<Condition> conditions;
        protected final List<String> stringConditions;
        protected final AbstractItemProvider rootItem;
        protected final AbstractItemProvider trueItem;
        protected final AbstractItemProvider falseItem;

        public ConditionalItem(List<String> conditions, Item rootItem, Item trueItem, Item falseItem) {
            this.stringConditions = conditions;
            this.rootItem = new ItemProvider(rootItem);
            this.trueItem = new ItemProvider(trueItem);
            this.falseItem = new ItemProvider(falseItem);

            this.conditions = new ArrayList<>();
            for (String condition : conditions)
                this.conditions.add(ConditionParser.parse(condition));
        }

    }

}
