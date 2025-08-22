package dev.rosewood.rosegarden.gui.action;

import org.bukkit.configuration.ConfigurationSection;

/**
 * An abstract class that should be extended to implement an {@linkplain Action action}.
 */
public abstract class AbstractAction implements Action {

    private final String id;

    public AbstractAction(String id) {
        this.id = id;
    }

    public AbstractAction(String id, ConfigurationSection section) {
        this(id);
    }

    @Override
    public String getId() {
        return this.id;
    }

}
