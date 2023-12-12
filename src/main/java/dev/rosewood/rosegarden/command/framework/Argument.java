package dev.rosewood.rosegarden.command.framework;

import java.util.Collection;

public sealed interface Argument {

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
     * @return a string representation of this argument as a parameter
     */
    default String parameter() {
        if (this.optional()) {
            return "[" + this.name() + "]";
        } else {
            return "<" + this.name() + ">";
        }
    }

    record CommandArgument<T>(int index,
                              String name,
                              boolean optional,
                              ArgumentHandler<T> handler) implements Argument { }

    record SubCommandArgument(int index,
                              String name,
                              boolean optional,
                              Collection<RoseCommand> subCommands) implements Argument {
    }

}




