package dev.rosewood.rosegarden.command.framework.command.suggest;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.handler.TestArgumentHandler;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;

public class SuggestionCommand extends BaseRoseCommand {

    public SuggestionCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void execute(CommandContext context) {
        throw new UnsupportedOperationException("Only for testing suggestions");
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("arg1", new TestArgumentHandler())
                .required("arg2", ArgumentHandlers.forEnum(TestEnum.class))
                .build();
    }

}
