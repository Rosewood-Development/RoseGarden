package dev.rosewood.rosegarden.command.framework;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface Argument {

    /**
     * @return The index of the argument in the command
     */
    int index();

    /**
     * @return The name of the argument
     */
    String name();

    /**
     * @return true if the argument is optional, false otherwise
     */
    boolean optional();

    /**
     * @return The condition that must be met for this argument to be used
     */
    ArgumentCondition condition();

    /**
     * @return a string representation of this argument as a parameter
     */
    default String parameter() {
        if (this.optional()) {
            return "[" + this.name() + "]";
        } else {
            return "<" + this.name() + ">";
        }
    }

    class CommandArgument<T> implements Argument {

        private final int index;
        private final String name;
        private final boolean optional;
        private final ArgumentCondition condition;
        private final ArgumentHandler<T> handler;

        public CommandArgument(int index,
                               String name,
                               boolean optional,
                               ArgumentCondition condition,
                               ArgumentHandler<T> handler) {
            this.index = index;
            this.name = name;
            this.optional = optional;
            this.condition = condition;
            this.handler = handler;
        }

        @Override
        public int index() {
            return this.index;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public boolean optional() {
            return this.optional;
        }

        @Override
        public ArgumentCondition condition() {
            return this.condition;
        }

        public ArgumentHandler<T> handler() {
            return this.handler;
        }

    }

    class SubCommandArgument implements Argument {

        private final int index;
        private final String name;
        private final boolean optional;
        private final ArgumentCondition condition;
        private final Collection<RoseCommand> subCommands;

        public SubCommandArgument(int index,
                                  String name,
                                  boolean optional,
                                  ArgumentCondition condition,
                                  Collection<RoseCommand> subCommands) {
            this.index = index;
            this.name = name;
            this.optional = optional;
            this.condition = condition;
            this.subCommands = subCommands;
        }
        @Override
        public int index() {
            return this.index;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public boolean optional() {
            return this.optional;
        }

        @Override
        public ArgumentCondition condition() {
            return this.condition;
        }

        public Collection<RoseCommand> subCommands() {
            return this.subCommands;
        }

    }

}




