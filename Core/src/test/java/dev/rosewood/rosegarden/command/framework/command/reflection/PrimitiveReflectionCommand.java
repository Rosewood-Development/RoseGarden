package dev.rosewood.rosegarden.command.framework.command.reflection;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.command.framework.handler.TestArgumentHandler;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;

public class PrimitiveReflectionCommand extends BaseRoseCommand {

    public PrimitiveReflectionCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        throw new IllegalStateException("Should never be called for this command");
    }

    @RoseExecutable
    public void execute(CommandContext context, int primitive) {
        context.getSender().sendMessage(String.valueOf(primitive));
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test")
                .arguments(ArgumentsDefinition.builder()
                        .required("arg1", ArgumentHandlers.INTEGER)
                        .build())
                .build();
    }

}
