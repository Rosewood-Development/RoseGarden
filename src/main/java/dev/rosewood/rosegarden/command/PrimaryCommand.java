package dev.rosewood.rosegarden.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.RoseGardenUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;

public abstract class PrimaryCommand extends BaseRoseCommand {

    private List<RoseCommand> subCommands;

    public PrimaryCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void baseExecute(CommandContext context) {
        AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);

        String baseColor = localeManager.getLocaleMessage("base-command-color");
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Running " + RoseGardenUtils.GRADIENT + this.rosePlugin.getDescription().getName() + baseColor + " v" + this.rosePlugin.getDescription().getVersion());
        localeManager.sendCustomMessage(context.getSender(), baseColor + "Plugin created by: <g:#41E0F0:#FF8DCE>" + this.rosePlugin.getDescription().getAuthors().get(0));
        localeManager.sendSimpleMessage(context.getSender(), "base-command-help", StringPlaceholders.of("cmd", this.getName()));
    }

    @Override
    protected final ArgumentsDefinition createArgumentsDefinition() {
        this.subCommands = new ArrayList<>(this.createSubCommands());

        CommandInfo helpCommandInfo = this.createHelpCommandInfo();
        if (helpCommandInfo != null)
            this.subCommands.add(new HelpCommand(this.rosePlugin, this, helpCommandInfo));

        CommandInfo reloadCommandInfo = this.createReloadCommandInfo();
        if (reloadCommandInfo != null)
            this.subCommands.add(new ReloadCommand(this.rosePlugin, reloadCommandInfo));

        return ArgumentsDefinition.builder()
                .optionalSub("subcommand", this.subCommands.toArray(RoseCommand[]::new));
    }

    protected abstract List<RoseCommand> createSubCommands();

    protected final List<RoseCommand> getSubCommands() {
        return this.subCommands;
    }

    /**
     * @return a non-null value if the command should include a help subcommand
     */
    public CommandInfo createHelpCommandInfo() {
        return CommandInfo.builder("help")
                .descriptionKey("command-help-description")
                .build();
    }

    /**
     * @return a non-null value if the command should include a reload subcommand
     */
    public CommandInfo createReloadCommandInfo() {
        return CommandInfo.builder("reload")
                .permission(this.rosePlugin.getName().toLowerCase() + ".reload")
                .descriptionKey("command-reload-description")
                .build();
    }

    /**
     * Override to provide a custom help message added to the bottom of the default help message
     *
     * @param context The command context
     */
    protected void sendCustomHelpMessage(CommandContext context) {
        // Provides no default behavior
    }

}
