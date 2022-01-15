package dev.rosewood.rosegarden.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractCommandManager;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.List;

public class HelpCommand extends RoseCommand {

    public HelpCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        AbstractCommandManager commandManager = this.rosePlugin.getManager(AbstractCommandManager.class);
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        localeManager.sendCommandMessage(context.getSender(), "command-help-title");
        for (RoseCommand command : commandManager.getCommands()) {
            if (!command.hasHelp() || !command.canUse(context.getSender()))
                continue;

            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("cmd", commandManager.getCommandName())
                    .addPlaceholder("subcmd", command.getName().toLowerCase())
                    .addPlaceholder("args", command.getArgumentsString())
                    .addPlaceholder("desc", localeManager.getLocaleMessage(command.getDescriptionKey()))
                    .build();
            localeManager.sendSimpleCommandMessage(context.getSender(), "command-help-list-description", stringPlaceholders);
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public String getDescriptionKey() {
        return "command-help-description";
    }

    @Override
    public String getRequiredPermission() {
        return null;
    }

}
