package dev.rosewood.menuexamples.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.ReloadCommand;
import dev.rosewood.rosegarden.command.framework.CommandInfo;

public class ReloadMenusCommand extends ReloadCommand {

    public ReloadMenusCommand(RosePlugin rosePlugin) {
        super(rosePlugin, CommandInfo.builder("reloadmenus")
                .descriptionKey("command-reload-description")
                .permission(rosePlugin.getName().toLowerCase() + ".reload")
                .build());
    }

}
