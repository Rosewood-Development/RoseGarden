package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.CommandException;

/**
 * The base class for all RoseGarden commands.
 * <p>
 * Override {@link #createArgumentsDefinition()} and {@link #createCommandInfo()} to customize the command.
 * <p>
 * To make executable methods in the command, annotate them with {@link RoseExecutable}. The first parameter will be
 * a {@link CommandContext} instance and the following parameters will be the command arguments as defined by the
 * {@link ArgumentsDefinition}. For example:
 * <blockquote><pre>
 *     @RoseExecutable
 *     public void execute(CommandContext context, /* additional parameters here *\/) {
 *         // ...
 *     }
 * <pre></blockquote>
 */
public abstract class BaseRoseCommand implements RoseCommand {

    protected final RosePlugin rosePlugin;
    private String activeName;
    private List<String> activeAliases;
    private CommandInfo commandInfo;
    private ArgumentsDefinition argumentsDefinition;

    public BaseRoseCommand(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    /**
     * @return a newly constructed {@link CommandInfo} for this command
     */
    protected abstract CommandInfo createCommandInfo();

    /**
     * @return a newly constructed {@link ArgumentsDefinition} for this command
     */
    protected ArgumentsDefinition createArgumentsDefinition() {
        return ArgumentsDefinition.empty();
    }

    protected final void setNameAndAliases(String name, List<String> aliases) {
        this.activeName = name;
        this.activeAliases = aliases;
    }

    protected final CommandInfo getCommandInfo() {
        if (this.commandInfo == null)
            this.commandInfo = this.createCommandInfo();
        return this.commandInfo;
    }

    @Override
    public final String getName() {
        if (this.activeName == null)
            this.activeName = this.getCommandInfo().name();
        return this.activeName;
    }

    @Override
    public final List<String> getAliases() {
        if (this.activeAliases == null)
            this.activeAliases = this.getCommandInfo().aliases();
        return this.activeAliases;
    }

    @Override
    public final String getPermission() {
        return this.getCommandInfo().permission();
    }

    @Override
    public final boolean isPlayerOnly() {
        return this.getCommandInfo().playerOnly();
    }

    @Override
    public final String getDescriptionKey() {
        return this.getCommandInfo().descriptionKey();
    }

    @Override
    public final ArgumentsDefinition getCommandArguments() {
        if (this.argumentsDefinition == null)
            this.argumentsDefinition = this.createArgumentsDefinition();
        return this.argumentsDefinition;
    }

    @Override
    public void execute(CommandContext context) {
        List<Method> methods = this.getExecuteMethods();
        if (!this.verifyMethods(methods))
            return;

        Optional<Method> method = methods.stream()
                .map(x -> new MethodScore(this.getParameterMatchScore(x, context.getUsedArgumentTypes()), x))
                .sorted(Comparator.comparingInt(MethodScore::score).reversed())
                .map(MethodScore::method)
                .findFirst();
        if (method.isEmpty()) {
            String arguments = Arrays.stream(context.getUsedArgumentTypes()).map(Class::getName).collect(Collectors.joining(", "));
            this.rosePlugin.getLogger().warning("No matching @RoseExecutable method found for command " + this.getCommandInfo().name() + ". Expected arguments (or similar matching optional arguments): [" + arguments + "]");
            return;
        }

        try {
            Method executeMethod = method.get();
            Object[] parameters = this.buildMethodParameters(context, executeMethod);

            executeMethod.invoke(this, parameters);
        } catch (ReflectiveOperationException e) {
            throw new CommandException("An error occurred while executing the command", e);
        }
    }

    private boolean verifyMethods(List<Method> methods) {
        if (methods.isEmpty()) {
            this.rosePlugin.getLogger().warning("No @RoseExecutable methods found for command " + this.getCommandInfo().name());
            return false;
        }

        for (Method method : methods) {
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 0 || parameters[0] != CommandContext.class) {
                this.rosePlugin.getLogger().warning("Invalid @RoseExecutable method for command " + this.getCommandInfo().name() + ". Must have a first parameter of type CommandContext.");
                return false;
            }
        }

        return true;
    }

    private int getParameterMatchScore(Method method, Class<?>[] argumentTypes) {
        Class<?>[] parameters = method.getParameterTypes();

        int score = 0;
        int maxItems = Math.max(parameters.length - 1, argumentTypes.length);
        for (int i = 0; i < maxItems; i++) {
            Class<?> methodType = this.arrayGetOrNull(parameters, i + 1);
            Class<?> argumentType = this.arrayGetOrNull(argumentTypes, i);

            if (methodType == null) {
                // Parameters do not match, do not consider this as an option
                return Integer.MIN_VALUE;
            } else if (argumentType == null) {
                // Parameters do not match, this can be null though as long as it isn't a primitive
                if (methodType.isPrimitive()) {
                    // Primitives can't be null, this can never match
                    return Integer.MIN_VALUE;
                } else {
                    // Parameters do not match, but can be nulled
                    score--;
                }
            } else if (methodType.isAssignableFrom(argumentType)) {
                // Parameters match perfectly
                score++;
            } else {
                // Parameters do not match, do not consider this as an option
                return Integer.MIN_VALUE;
            }
        }

        return score;
    }

    private <T> T arrayGetOrNull(T[] array, int index) {
        return index >= 0 && index < array.length ? array[index] : null;
    }

    protected Object[] buildMethodParameters(CommandContext context, Method method) {
        Object[] parameters = new Object[method.getParameterCount()];
        parameters[0] = context;
        for (int i = 1; i < parameters.length; i++)
            parameters[i] = context.get(i - 1);
        return parameters;
    }

    private record MethodScore(int score, Method method) implements Comparable<MethodScore> {

        @Override
        public int compareTo(BaseRoseCommand.MethodScore o) {
            return Integer.compare(this.score, o.score);
        }

    }

}
