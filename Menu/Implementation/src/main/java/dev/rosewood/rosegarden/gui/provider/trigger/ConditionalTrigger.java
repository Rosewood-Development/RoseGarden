package dev.rosewood.rosegarden.gui.provider.trigger;

import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.action.type.ConditionalAction;
import java.util.Arrays;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

public class ConditionalTrigger extends TriggerProvider {

    public ConditionalTrigger(String key, ConfigurationSection section) {
        super(key, section);
    }

    public ConditionalTrigger(Action... actions) {
        super(ConditionalAction.ID, Arrays.asList(actions));
    }

    public ConditionalTrigger(List<Action> actions) {
        super(ConditionalAction.ID, actions);
    }

    public ConditionalTrigger action(Action action) {
        this.actions.add(action);
        return this;
    }

    @Override
    public void write(ConfigurationSection section) {
        for (int i = 0; i < this.actions.size(); i++) {
            Action action = this.actions.get(i);
            ConfigurationSection idSection = section.createSection(String.valueOf(i));

            idSection.set("type", action.getId());
            action.write(idSection);
        }
    }

}
