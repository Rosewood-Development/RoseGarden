package dev.rosewood.rosegarden.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;

public class ReloadCommand extends BaseRoseCommand {

    private final CommandInfo commandInfo;

    public ReloadCommand(RosePlugin rosePlugin, CommandInfo commandInfo) {
        super(rosePlugin);

        this.commandInfo = commandInfo;
    }

    public ReloadCommand(RosePlugin rosePlugin) {
        this(rosePlugin, CommandInfo.builder("reload")
                .descriptionKey("command-reload-description")
                .permission(rosePlugin.getName().toLowerCase() + ".reload")
                .build());
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return this.commandInfo;
    }

    @RoseExecutable
    public void invoke(CommandContext context) {
        this.rosePlugin.reload();
        this.rosePlugin.getManager(AbstractLocaleManager.class).sendCommandMessage(context.getSender(), "command-reload-reloaded");
    }

}
