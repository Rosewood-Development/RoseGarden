package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.condition.Condition;
import dev.rosewood.rosegarden.gui.condition.ConditionParser;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.trigger.ConditionalTrigger;
import org.bukkit.configuration.ConfigurationSection;
import java.util.List;

/**
 * An action which runs other actions based on whether a {@link Condition} passes or fails.
 *
 * Config:
 * trigger-type:
 *   requires-condition:
 *     condition: "%some_condition%"
 *     pass:
 *       another-action: true
 *     fail:
 *       another-action: true
 */
public class ConditionalAction extends AbstractAction {

    // Unique ID of the action.
    public static final String ID = "requires-condition";

    protected final String rawCondition;
    protected final Condition condition;
    protected final ConditionalTrigger passTrigger;
    protected final ConditionalTrigger failTrigger;

    // Code Constructors

    public ConditionalAction(String condition, List<Action> passActions, List<Action> failActions) {
        super(ID);

        this.rawCondition = condition;
        this.condition = condition != null ? ConditionParser.parse(condition) : null;
        this.passTrigger = new ConditionalTrigger(passActions);
        this.failTrigger = new ConditionalTrigger(failActions);
    }

    // Config Constructors

    public ConditionalAction(ConfigurationSection section) {
        super(ID, section);

        // Grab the condition from the config and parse it.
        this.rawCondition = section.getString(ID + ".condition");
        this.condition = this.rawCondition != null ? ConditionParser.parse(this.rawCondition) : null;

        // Create a conditional trigger for the 'pass' and 'fail' sections.
        this.passTrigger = new ConditionalTrigger(ID + ".pass", section);
        this.failTrigger = new ConditionalTrigger(ID + ".fail", section);
    }

    // Serialize the action.
    @Override
    public void write(ConfigurationSection section) {
        if (this.condition == null)
            return;

        // Write the condition and allow the triggers to write too.
        ConfigurationSection conditionSection = section.createSection(ID);
        conditionSection.set("condition", this.rawCondition);
        this.passTrigger.write(conditionSection.createSection("pass"));
        this.failTrigger.write(conditionSection.createSection("fail"));
    }

    @Override
    public void run(Context context) {
        // Run the pass trigger if the condition passes, or the fail trigger if the condition fails.
        if (this.condition.check(context))
            this.passTrigger.call(context);
        else
            this.failTrigger.call(context);
    }

    public Condition getCondition() {
        return this.condition;
    }

    public ConditionalTrigger getPassTrigger() {
        return this.passTrigger;
    }

    public ConditionalTrigger getFailTrigger() {
        return this.failTrigger;
    }

    // Static Constructors

    public static ConditionalAction of(String condition, List<Action> passActions, List<Action> failActions) {
        return new ConditionalAction(condition, passActions, failActions);
    }

}

