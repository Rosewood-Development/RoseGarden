package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;

public abstract class Manager {

    protected final RosePlugin rosePlugin;

    public Manager(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    /**
     * Reloads the Manager's settings
     */
    public abstract void reload();

    /**
     * Cleans up the Manager's resources
     */
    public abstract void disable();

}
