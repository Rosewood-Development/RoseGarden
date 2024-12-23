package dev.rosewood.rosegarden.command.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArgumentsDefinition {

    private static final ArgumentsDefinition EMPTY = new ArgumentsDefinition(Collections.emptyList());

    private final List<Argument> arguments;

    public ArgumentsDefinition(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public Argument get(int index) {
        return this.arguments.get(index);
    }

    public int size() {
        return this.arguments.size();
    }

    public String getParametersString(CommandContext context) {
        return getParametersString(context, this.arguments);
    }

    public static String getParametersString(CommandContext context, List<Argument> arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Argument argument : arguments) {
            if (!argument.condition().test(context))
                continue;

            String[] rawArguments = context.getRawArguments(argument);
            if (rawArguments.length > 0) {
                for (String arg : rawArguments)
                    stringBuilder.append(arg).append(' ');
            } else {
                stringBuilder.append(argument.parameter()).append(' ');
            }
        }
        return stringBuilder.toString().trim();
    }

    public static ArgumentsDefinition empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static <T> ArgumentsDefinition of(String name1, ArgumentHandler<T> handler1) {
        return new Builder()
                .required(name1, handler1)
                .build();
    }

    public static <T> ArgumentsDefinition of(String name1, ArgumentHandler<T> handler1,
                                             String name2, ArgumentHandler<T> handler2) {
        return new Builder()
                .required(name1, handler1)
                .required(name2, handler2)
                .build();
    }

    public static <T> ArgumentsDefinition of(String name1, ArgumentHandler<T> handler1,
                                             String name2, ArgumentHandler<T> handler2,
                                             String name3, ArgumentHandler<T> handler3) {
        return new Builder()
                .required(name1, handler1)
                .required(name2, handler2)
                .required(name3, handler3)
                .build();
    }

    public static class Builder {

        private final List<Argument> arguments;

        private Builder() {
            this.arguments = new ArrayList<>();
        }

        public <T> Builder required(String name, ArgumentHandler<T> handler) {
            this.arguments.add(new Argument.CommandArgument<>(name, false, c -> true, handler));
            return this;
        }

        public <T> Builder optional(String name, ArgumentHandler<T> handler) {
            this.arguments.add(new Argument.CommandArgument<>(name, true, c -> true, handler));
            return this;
        }

        public <T> Builder optional(String name, ArgumentHandler<T> handler, ArgumentCondition condition) {
            this.arguments.add(new Argument.CommandArgument<>(name, true, condition, handler));
            return this;
        }

        public ArgumentsDefinition requiredSub(String name, RoseCommand... subCommands) {
            if (subCommands.length == 0)
                throw new IllegalArgumentException("subCommands cannot be empty");
            this.arguments.add(new Argument.SubCommandArgument(name, false, c -> true, Arrays.asList(subCommands)));
            return new ArgumentsDefinition(this.arguments);
        }

        public ArgumentsDefinition requiredSub(RoseCommand... subCommands) {
            return this.requiredSub("subcommand", subCommands);
        }

        public ArgumentsDefinition optionalSub(String name, RoseCommand... subCommands) {
            if (subCommands.length == 0)
                throw new IllegalArgumentException("subCommands cannot be empty");
            this.arguments.add(new Argument.SubCommandArgument(name, true, c -> true, Arrays.asList(subCommands)));
            return new ArgumentsDefinition(this.arguments);
        }

        public ArgumentsDefinition optionalSub(RoseCommand... subCommands) {
            return this.optionalSub("subcommand", subCommands);
        }

        public ArgumentsDefinition optionalSub(String name, ArgumentCondition condition, RoseCommand... subCommands) {
            if (subCommands.length == 0)
                throw new IllegalArgumentException("subCommands cannot be empty");
            this.arguments.add(new Argument.SubCommandArgument(name, true, condition, Arrays.asList(subCommands)));
            return new ArgumentsDefinition(this.arguments);
        }

        public ArgumentsDefinition optionalSub(ArgumentCondition condition, RoseCommand... subCommands) {
            return this.optionalSub("subcommand", condition, subCommands);
        }

        public ArgumentsDefinition build() {
            return new ArgumentsDefinition(this.arguments);
        }

    }

}
