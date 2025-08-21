package dev.rosewood.menuexamples.command;

import dev.rosewood.menuexamples.command.argument.MenuArgumentHandler;
import dev.rosewood.menuexamples.manager.GuiManager;
import dev.rosewood.menuexamples.manager.LocaleManager;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import org.bukkit.entity.Player;

public class MenuCommand extends BaseRoseCommand {

    public MenuCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, RoseMenuWrapper menu, Player player) {
        if (player == null && context.getSender() instanceof Player sender)
            player = sender;

        if (player == null) {
            RosePlugin.instance().getManager(LocaleManager.class).sendCommandMessage(context.getSender(), "command-menu-console");
            return;
        }

        RosePlugin.instance().getManager(GuiManager.class).open(menu, player);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("menu")
                .descriptionKey("command-menu-description")
                .permission("menuexamples.menu")
                .arguments(ArgumentsDefinition.builder()
                        .required("menu", MenuArgumentHandler.INSTANCE)
                        .optional("player", ArgumentHandlers.PLAYER)
                        .build())
                .build();
    }

}
