package dev.rosewood.rosegarden.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.List;

public class BaseCommand extends RoseCommand {

    public BaseCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        String baseColor = localeManager.getLocaleMessage("base-command-color");
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Running " + RoseGardenUtils.GRADIENT + this.rosePlugin.getDescription().getName() + baseColor + " v" + this.rosePlugin.getDescription().getVersion());
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + this.rosePlugin.getDescription().getAuthors().get(0));
        localeManager.sendSimpleMessage(context.getSender(), "base-command-help", StringPlaceholders.of("cmd", this.parent.getName()));
    }

    @Override
    public String getDefaultName() {
        return "";
    }

    @Override
    public List<String> getDefaultAliases() {
        return Collections.emptyList();
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
    public boolean hasHelp() {
        return false;
    }

    /**
     * @return the override command name, or null if this command should be executed as normal
     */
    public String getOverrideCommand() {
        return null;
    }

}
