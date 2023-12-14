package dev.rosewood.rosegarden.command.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentsDefinition {

    private static final ArgumentsDefinition EMPTY = new ArgumentsDefinition(List.of());

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

    public static ArgumentsDefinition empty() {
        return EMPTY;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final List<Argument> arguments;

        private Builder() {
            this.arguments = new ArrayList<>();
        }

        public <T> Builder required(String name, ArgumentHandler<T> handler) {
            this.arguments.add(new Argument.CommandArgument<>(this.arguments.size(), name, false, handler));
            return this;
        }

        public <T> OptionalOnlyBuilder optional(String name, ArgumentHandler<T> handler) {
            this.arguments.add(new Argument.CommandArgument<>(this.arguments.size(), name, true, handler));
            return new OptionalOnlyBuilder(this);
        }

        public ArgumentsDefinition requiredSub(String name, RoseCommand... subCommands) {
            if (subCommands.length == 0)
                throw new IllegalArgumentException("subCommands cannot be empty");
            this.arguments.add(new Argument.SubCommandArgument(this.arguments.size(), name, false, Arrays.asList(subCommands)));
            return new ArgumentsDefinition(this.arguments);
        }

        public ArgumentsDefinition optionalSub(String name, RoseCommand... subCommands) {
            if (subCommands.length == 0)
                throw new IllegalArgumentException("subCommands cannot be empty");
            this.arguments.add(new Argument.SubCommandArgument(this.arguments.size(), name, true, Arrays.asList(subCommands)));
            return new ArgumentsDefinition(this.arguments);
        }

        public ArgumentsDefinition requiredSub(RoseCommand... subCommands) {
            return this.requiredSub("subcommand", subCommands);
        }

        public ArgumentsDefinition optionalSub(RoseCommand... subCommands) {
            return this.optionalSub("subcommand", subCommands);
        }

        public ArgumentsDefinition build() {
            return new ArgumentsDefinition(this.arguments);
        }

    }

    public static class OptionalOnlyBuilder {

        private final Builder originalBuilder;

        private OptionalOnlyBuilder(Builder originalBuilder) {
            this.originalBuilder = originalBuilder;
        }

        public <T> Builder optional(String name, ArgumentHandler<T> handler) {
            this.originalBuilder.optional(name, handler);
            return this.originalBuilder;
        }

        public ArgumentsDefinition optionalSub(String name, RoseCommand... subCommands) {
            return this.originalBuilder.optionalSub(name, subCommands);
        }

        public ArgumentsDefinition optionalSub(RoseCommand... subCommands) {
            return this.originalBuilder.optionalSub(subCommands);
        }

        public ArgumentsDefinition build() {
            return this.originalBuilder.build();
        }

    }

}
