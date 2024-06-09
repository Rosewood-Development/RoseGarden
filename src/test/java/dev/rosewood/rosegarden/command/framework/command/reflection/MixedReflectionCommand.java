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

public class MixedReflectionCommand extends BaseRoseCommand {

    public MixedReflectionCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        throw new IllegalStateException("Should never be called for this command");
    }

    @RoseExecutable
    public void execute(CommandContext context, String object) {
        context.getSender().sendMessage(object);
    }

    @RoseExecutable
    public void execute(CommandContext context, TestEnum value, String object) {
        context.getSender().sendMessage(value.name());
        context.getSender().sendMessage(object);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test")
                .arguments(ArgumentsDefinition.builder()
                        .optional("arg1", ArgumentHandlers.forEnum(TestEnum.class))
                        .required("arg2", new TestArgumentHandler())
                        .build())
                .build();
    }

}
