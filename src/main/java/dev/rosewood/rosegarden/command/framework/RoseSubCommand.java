package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import java.util.List;

/**
 * Create one or more subclasses within a {@link RoseCommand} that extend this class.
 */
public abstract class RoseSubCommand extends RoseCommand {

    public RoseSubCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
        super.setNameAndAliases(this.getDefaultName(), this.getDefaultAliases());
    }

    @Override
    protected final void setNameAndAliases(String name, List<String> aliases) {
        throw new IllegalStateException("RoseSubCommands cannot have their name or aliases changed");
    }

    @Override
    public String getDescriptionKey() {
        return null;
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

    @Override
    public final boolean hasHelp() {
        return false;
    }

}
