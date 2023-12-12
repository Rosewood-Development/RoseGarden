package dev.rosewood.rosegarden.command.framework.command.required;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;

public class TwoArgsCommand extends BaseRoseCommand {

    public TwoArgsCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void execute(CommandContext context) {
        TestEnum value = context.get("arg1");
        TestEnum value2 = context.get("arg2");

        context.getSender().sendMessage(value.name());
        context.getSender().sendMessage(value2.name());
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("arg1", ArgumentHandlers.forEnum(TestEnum.class))
                .required("arg2", ArgumentHandlers.forEnum(TestEnum.class))
                .build();
    }

}
