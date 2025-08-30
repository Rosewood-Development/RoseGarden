package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.condition.ConditionParser;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.trigger.ConditionalTrigger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action which runs other actions based on whether a {@linkplain Condition condition} passes or fails.<br>
 *
 * Usage:<br>
 * <pre>
 *     {@code
 *     trigger-type:
 *       0:
 *         type: conditional
 *         conditions:
 *           - "%some_condition%"
 *         true:
 *           type: other
 *         false:
 *           type: other
 *     }
 * </pre>
 */
public class ConditionalAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "conditional";

    public static final String CONDITIONS = "conditions";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    protected final List<String> rawConditions;
    protected final List<Condition> conditions;
    protected final ConditionalTrigger trueTrigger;
    protected final ConditionalTrigger falseTrigger;

    // Code Constructors

    public ConditionalAction(List<String> conditions, Action trueAction, Action falseAction) {
        super(ID);

        this.rawConditions = conditions;
        this.conditions = new ArrayList<>();
        for (String condition : this.rawConditions)
            this.conditions.add(ConditionParser.parse(condition));

        this.trueTrigger = new ConditionalTrigger(trueAction);
        this.falseTrigger = new ConditionalTrigger(falseAction);
    }

    public ConditionalAction(Action trueAction, Action falseAction, String... conditions) {
        this(Arrays.asList(conditions), trueAction, falseAction);
    }

    // Config Constructors

    public ConditionalAction(ConfigurationSection section) {
        super(ID, section);

        // Grab the condition from the config and parse it.
        this.rawConditions = section.getStringList(CONDITIONS);
        this.conditions = new ArrayList<>();
        for (String condition : this.rawConditions)
            this.conditions.add(ConditionParser.parse(condition));

        // Create a conditional trigger for the 'true' and 'false' sections.
        this.trueTrigger = new ConditionalTrigger(TRUE, section);
        this.falseTrigger = new ConditionalTrigger(FALSE, section);
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        if (this.rawConditions.isEmpty())
            return;

        // Write the condition and allow the triggers to write too.
        section.set(CONDITIONS, this.rawConditions);
        this.trueTrigger.write(section.createSection(TRUE));
        this.falseTrigger.write(section.createSection(FALSE));
    }

    @Override
    public void run(Context context) {
        for (Condition condition : this.conditions) {
            if (!condition.check(context)) {
                this.falseTrigger.call(context);
                return;
            }
        }

        this.trueTrigger.call(context);
    }

    public List<Condition> getConditions() {
        return this.conditions;
    }

    public ConditionalTrigger getTrueTrigger() {
        return this.trueTrigger;
    }

    public ConditionalTrigger getFalseTrigger() {
        return this.falseTrigger;
    }

    // Static Constructors

    public static ConditionalAction of(List<String> conditions, Action trueAction, Action falseAction) {
        return new ConditionalAction(conditions, trueAction, falseAction);
    }

    public static ConditionalAction of(Action trueAction, Action falseAction, String... conditions) {
        return new ConditionalAction(trueAction, falseAction, conditions);
    }

}

