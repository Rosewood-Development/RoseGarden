package dev.rosewood.rosegarden.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

public class HelpCommand extends BaseRoseCommand {

    private final PrimaryCommand parent;
    private final CommandInfo commandInfo;

    public HelpCommand(RosePlugin rosePlugin, PrimaryCommand parent, CommandInfo commandInfo) {
        super(rosePlugin);

        this.parent = parent;
        this.commandInfo = commandInfo;
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return this.commandInfo;
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        localeManager.sendCommandMessage(context.getSender(), "command-help-title");
        for (RoseCommand command : this.parent.getSubCommands()) {
            if (!command.canUse(context.getSender()))
                continue;

            StringPlaceholders stringPlaceholders = StringPlaceholders.of(
                    "cmd", context.getCommandLabel().toLowerCase(),
                    "subcmd", command.getName().toLowerCase(),
                    "args", command.getParametersString(),
                    "desc", localeManager.getLocaleMessage(command.getDescriptionKey())
            );

            localeManager.sendSimpleCommandMessage(context.getSender(), "command-help-list-description" + (command.getCommandArguments().size() == 0 ? "-no-args" : ""), stringPlaceholders);
        }

        this.parent.sendCustomHelpMessage(context);
    }

}
