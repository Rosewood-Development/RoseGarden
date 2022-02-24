package dev.rosewood.rosegarden.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import java.util.Collections;
import java.util.List;

public class ReloadCommand extends RoseCommand {

    public ReloadCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        this.rosePlugin.reload();
        this.rosePlugin.getManager(AbstractLocaleManager.class).sendCommandMessage(context.getSender(), "command-reload-reloaded");
    }

    @Override
    public String getDefaultName() {
        return "reload";
    }

    @Override
    public List<String> getDefaultAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescriptionKey() {
        return "command-reload-description";
    }

    @Override
    public String getRequiredPermission() {
        return this.rosePlugin.getName().toLowerCase() + ".reload";
    }

}
