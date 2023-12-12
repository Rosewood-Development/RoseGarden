package dev.rosewood.rosegarden.command.framework.command.optional;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;

public class OneArgsCommand extends BaseRoseCommand {

    public OneArgsCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void execute(CommandContext context) {
        TestEnum value = context.get("arg1");

        context.getSender().sendMessage(value + "");
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .optional("arg1", ArgumentHandlers.forEnum(TestEnum.class))
                .build();
    }

}
