package dev.rosewood.rosegarden.command.framework.command.mixed;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.command.framework.ArgumentCondition;
import java.util.Objects;

public class MixedArgsCommand extends BaseRoseCommand {

    public static final String TEST_PERMISSION = "rosegarden.test.mixedargs";

    public MixedArgsCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @RoseExecutable
    public void execute(CommandContext context, String value, String value2) {
        context.getSender().sendMessage(Objects.requireNonNullElse(value, "null"));
        context.getSender().sendMessage(value2);
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .optional("arg1", ArgumentHandlers.forValues(String.class, "alice", "bob"), ArgumentCondition.hasPermission(TEST_PERMISSION))
                .required("arg2", ArgumentHandlers.forValues(String.class, "on", "off"))
                .build();
    }

}
