package dev.rosewood.rosegarden.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

public abstract class PrimaryCommand extends BaseRoseCommand {

    public PrimaryCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        String baseColor = localeManager.getLocaleMessage("base-command-color");
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Running " + RoseGardenUtils.GRADIENT + this.rosePlugin.getDescription().getName() + baseColor + " v" + this.rosePlugin.getDescription().getVersion());
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + this.rosePlugin.getDescription().getAuthors().get(0));
        localeManager.sendSimpleMessage(context.getSender(), "base-command-help", StringPlaceholders.of("cmd", context.getCommandLabel()));
    }

}
