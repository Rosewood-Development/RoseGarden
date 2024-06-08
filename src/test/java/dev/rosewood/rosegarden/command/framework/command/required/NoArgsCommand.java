package dev.rosewood.rosegarden.command.framework.command.required;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;

public class NoArgsCommand extends BaseRoseCommand {

    public static final String SUCCESS_OUTPUT = "success";

    public NoArgsCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        context.getSender().sendMessage(SUCCESS_OUTPUT);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

}
