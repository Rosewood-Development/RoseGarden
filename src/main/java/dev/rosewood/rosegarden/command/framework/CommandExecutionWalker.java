package dev.rosewood.rosegarden.command.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CommandExecutionWalker {

    private RoseCommand command;
    private ArgumentsDefinition currentDefinition;
    private int argumentIndex;
    private boolean exited;
    private boolean completed;

    public CommandExecutionWalker(RoseCommand command) {
        this.command = command;
        this.currentDefinition = command.getCommandArguments();
        this.argumentIndex = 0;
        this.exited = false;
        this.completed = this.currentDefinition.size() == 0;
    }

    /**
     * Executes the next step of the walker.
     *
     * @param walker The walker to apply to each argument-command pair.
     * @param subCommandSelector The function to select a sub-command from a sub-command argument.
     * @throws IllegalStateException If the walker has already finished.
     */
    public void step(BiFunction<RoseCommand, Argument.CommandArgument<?>, Boolean> walker, Function<Argument.SubCommandArgument, RoseCommand> subCommandSelector) {
        if (this.completed)
            throw new IllegalStateException("Walker has already finished executing");

        Argument argument = this.currentDefinition.get(this.argumentIndex);
        if (argument instanceof Argument.SubCommandArgument) {
            Argument.SubCommandArgument subCommandArgument = (Argument.SubCommandArgument) argument;
            RoseCommand nextCommand = subCommandSelector.apply(subCommandArgument);
            if (nextCommand == null) {
                this.exited = true;
                return;
            }

            this.command = nextCommand;
            this.currentDefinition = this.command.getCommandArguments();
            this.argumentIndex = 0;
        } else if (argument instanceof Argument.CommandArgument<?>) {
            Argument.CommandArgument<?> commandArgument = (Argument.CommandArgument<?>) argument;
            if (!walker.apply(this.command, commandArgument)) {
                this.exited = true;
                return;
            }

            this.argumentIndex++;
        }

        if (this.argumentIndex >= this.currentDefinition.size())
            this.completed = true;
    }

    /**
     * @return The remaining arguments
     */
    public List<Argument> walkRemaining() {
        if (this.completed)
            return Collections.emptyList();

        List<Argument> remaining = new ArrayList<>();
        while (this.hasNext()) {
            this.step((command, argument) -> {
                remaining.add(argument);
                return true;
            }, argument -> {
                remaining.add(argument);
                return null;
            });
        }
        this.completed = true;
        return remaining;
    }

    /**
     * @return true if the walker has another step to execute, false otherwise
     */
    public boolean hasNext() {
        return !this.exited && !this.completed;
    }

    /**
     * @return true if the next argument is optional, false otherwise
     */
    public boolean hasNextStep() {
        return this.currentDefinition.size() > this.argumentIndex + 1;
    }

    /**
     * @return true if the walker has executed successfully to the last step, false otherwise
     */
    public boolean isCompleted() {
        return this.completed && this.command != null;
    }

    public RoseCommand getCurrentCommand() {
        return this.command;
    }

}
