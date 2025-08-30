package dev.rosewood.rosegarden.gui.provider.trigger;

import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.action.Actions;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.AbstractProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A {@linkplain AbstractProvider provider} that holds a list of {@linkplain Action actions}.<br>
 * Used for calling actions when an event happens.
 */
public class TriggerProvider extends AbstractProvider<List<Action>> {

    protected final String trigger;
    protected final List<Action> actions;

    public TriggerProvider(String trigger, Action... actions) {
        super(null, null);

        this.trigger = trigger;
        this.actions = new ArrayList<>(Arrays.asList(actions));
    }

    public TriggerProvider(String trigger, List<Action> actions) {
        super(null, null);

        this.trigger = trigger;
        this.actions = actions;
    }

    public TriggerProvider(String key, ConfigurationSection section) {
        super(key, section);

        this.trigger = key;
        this.actions = new ArrayList<>();
        if (section.isConfigurationSection(key)) {
            ConfigurationSection triggerSection = section.getConfigurationSection(key);
            if (triggerSection == null)
                return;

            for (String id : triggerSection.getKeys(false)) {
                ConfigurationSection idSection = triggerSection.getConfigurationSection(id);
                if (idSection == null)
                    continue;

                String actionId = idSection.getString("type");

                Actions.ActionType<?> actionType = Actions.getRegistry().get(actionId);
                if (actionType == null)
                    continue;

                Action action = (Action) actionType.create(idSection);
                this.actions.add(action);
            }
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        ConfigurationSection triggerSection = section.createSection(this.trigger);

        for (int i = 0; i < this.actions.size(); i++) {
            Action action = this.actions.get(i);
            ConfigurationSection idSection = triggerSection.createSection(String.valueOf(i));

            idSection.set("type", action.getId());
            action.write(idSection);
        }
    }

    @Override
    public List<Action> get(Context context) {
        return this.actions;
    }

    public void add(Action action) {
        this.actions.add(action);
    }

    public void call(Context context) {
        for (Action action : this.actions)
            action.run(context);
    }

    @Override
    public String getGroup() {
        return this.trigger;
    }

    @Override
    public String getId() {
        return this.trigger;
    }

}
