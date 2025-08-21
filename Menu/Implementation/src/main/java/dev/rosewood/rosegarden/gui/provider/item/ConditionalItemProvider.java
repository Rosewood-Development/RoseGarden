package dev.rosewood.rosegarden.gui.provider.item;

import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.condition.ConditionParser;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Optional;

public class ConditionalItemProvider extends AbstractItemProvider {

    public static final String ID = "requires-condition";

    protected final String conditionStr;
    protected final Condition condition;
    protected final AbstractItemProvider passItem;
    protected final AbstractItemProvider failItem;

    public ConditionalItemProvider(String condition, Item passItem, Item failItem) {
        super(ID, null);

        this.conditionStr = condition;
        this.condition = ConditionParser.parse(this.conditionStr);
        this.passItem = new ItemProvider(passItem);
        this.failItem = new ItemProvider(failItem);
    }

    public ConditionalItemProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.conditionStr = section.getString(key + ".condition");
        this.condition = this.conditionStr != null ? ConditionParser.parse(this.conditionStr) : null;
        this.passItem = new CompositeItemProvider(key + ".pass", section);
        this.failItem = new CompositeItemProvider(key + ".fail", section);
    }

    @Override
    public void write(ConfigurationSection section) {
        section.set(this.getKey() + ".condition", this.conditionStr);
        this.passItem.write(section.createSection(this.getKey() + ".pass"));
        this.failItem.write(section.createSection(this.getKey() + ".fail"));
    }

    @Override
    public RoseItem get(Context context) {
        Optional<RoseItem> item = context.get(Parameters.ITEM);

        return item.orElse(RoseItem.empty()).mergeWith(this.condition.check(context) ? this.passItem.get(context)
                : this.failItem.get(context));
    }

}
