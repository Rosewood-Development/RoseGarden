package dev.rosewood.rosegarden.gui.action;

import dev.rosewood.rosegarden.gui.parameter.Context;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action that runs when it is triggered by a {@linkplain dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider TriggerProvider}.
 */
@FunctionalInterface
public interface Action {

    void run(Context context);

    default void write(ConfigurationSection section) {

    }

    default String getId() {
        return null;
    }

}
