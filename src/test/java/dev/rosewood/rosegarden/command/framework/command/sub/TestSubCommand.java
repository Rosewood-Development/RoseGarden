package dev.rosewood.rosegarden.command.framework.command.sub;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.argument.ArgumentHandlers;
import dev.rosewood.rosegarden.command.framework.ArgumentsDefinition;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.CommandInfo;
import dev.rosewood.rosegarden.command.framework.model.TestEnum;

public class TestSubCommand extends BaseRoseCommand {

    public TestSubCommand(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void execute(CommandContext context) {
        TestEnum input = context.get("arg1");

        context.getSender().sendMessage(input.name());
    }

    @Override
    protected CommandInfo createCommandInfo() {
        return CommandInfo.builder("test").build();
    }

    @Override
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.builder()
                .required("arg1", ArgumentHandlers.forEnum(TestEnum.class))
                .optionalSub("arg2",
                        new Option1(),
                        new Option2(),
                        new Option3()
                );
    }

    private class Option1 extends BaseRoseCommand {

        public Option1() {
            super(TestSubCommand.this.rosePlugin);
        }

        @Override
        public void execute(CommandContext context) {
            TestSubCommand.this.execute(context);

            context.getSender().sendMessage("option1");

            String arg3 = context.get("arg3");
            if (arg3 != null)
                context.getSender().sendMessage(arg3);
        }

        @Override
        protected CommandInfo createCommandInfo() {
            return CommandInfo.builder("option1").build();
        }

        @Override
        protected ArgumentsDefinition createArgumentsDefinition() {
            return ArgumentsDefinition.builder()
                    .optional("arg3", ArgumentHandlers.STRING)
                    .build();
        }

    }

    private class Option2 extends BaseRoseCommand {

        public Option2() {
            super(TestSubCommand.this.rosePlugin);
        }

        @Override
        public void execute(CommandContext context) {
            TestSubCommand.this.execute(context);

            String arg3 = context.get("arg3");

            context.getSender().sendMessage("option2");
            context.getSender().sendMessage(arg3);
        }

        @Override
        protected CommandInfo createCommandInfo() {
            return CommandInfo.builder("option2").build();
        }

        @Override
        protected ArgumentsDefinition createArgumentsDefinition() {
            return ArgumentsDefinition.builder()
                    .required("arg3", ArgumentHandlers.STRING)
                    .build();
        }

    }

    private class Option3 extends BaseRoseCommand {

        public Option3() {
            super(TestSubCommand.this.rosePlugin);
        }

        @Override
        public void execute(CommandContext context) {
            TestSubCommand.this.execute(context);

            context.getSender().sendMessage("secret-option3");
        }

        @Override
        protected CommandInfo createCommandInfo() {
            return CommandInfo.builder("secret-option3").build();
        }

        @Override
        protected ArgumentsDefinition createArgumentsDefinition() {
            return ArgumentsDefinition.builder()
                    .required("arg3", ArgumentHandlers.BOOLEAN)
                    .build();
        }

    }

}
