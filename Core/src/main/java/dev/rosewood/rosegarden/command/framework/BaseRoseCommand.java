package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.ClassUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.command.CommandException;

/**
 * The base class for all RoseGarden commands.
 * <p>
 * Override {@link #createCommandInfo()} to customize the command.
 * <p>
 * To make executable methods in the command, annotate them with {@link RoseExecutable}. The first parameter will be
 * a {@link CommandContext} instance and the following parameters will be the command arguments as defined by the
 * {@link ArgumentsDefinition}. For example:
 * <blockquote><pre>
 * \@RoseExecutable
 * public void execute(CommandContext context, /* additional parameters here *\/) {
 *     // ...
 * }<pre></blockquote>
 * <p>
 * Additionally, make sure to provide the arguments for the command in the {@link ArgumentsDefinition} provided through
 * {@link #createCommandInfo()}.
 */
public abstract class BaseRoseCommand implements RoseCommand {

    protected final RosePlugin rosePlugin;
    private String activeName;
    private List<String> activeAliases;
    private CommandInfo commandInfo;

    public BaseRoseCommand(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    /**
     * @return a newly constructed {@link CommandInfo} for this command
     */
    protected abstract CommandInfo createCommandInfo();

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
        return this.commandInfo.arguments();
    }

    /**
     * @return true to make this command register over other commands in the Bukkit command map
     */
    protected boolean hasPriority() {
        return false;
    }

    @Override
    public void invoke(CommandContext context) {
        List<Method> methods = this.getExecuteMethods();
        if (!this.verifyMethods(methods))
            return;

        Optional<Method> method = methods.stream()
                .map(x -> new MethodScore(this.getParameterMatchScore(x.getParameterTypes(), context.getUsedArgumentTypes()), x))
                .sorted(Comparator.comparingInt(MethodScore::score).reversed())
                .map(MethodScore::method)
                .findFirst();

        if (!method.isPresent()) {
            String arguments = Arrays.stream(context.getUsedArgumentTypes()).map(Class::getName).collect(Collectors.joining(", "));
            this.rosePlugin.getLogger().warning("No matching @RoseExecutable method found for command " + this.getCommandInfo().name() + ". Expected arguments (or similar matching optional arguments): [" + arguments + "]");
            return;
        }

        try {
            Method executeMethod = method.get();
            Object[] parameters = this.buildMethodParameters(context, executeMethod);

            executeMethod.setAccessible(true); // Nested class execute methods cannot be accessed without this
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

    private int getParameterMatchScore(Class<?>[] methodTypes, Class<?>[] argumentTypes) {
        int score = 0;
        int maxItems = Math.max(methodTypes.length - 1, argumentTypes.length);
        for (int i = 0; i < maxItems; i++) {
            Class<?> methodType = this.arrayGetOrNull(methodTypes, i + 1);
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
                    // Parameters do not match but can be nulled, allow this to match if no better alternatives are found
                    score--;
                }
            } else if (methodType.isAssignableFrom(argumentType) || Objects.equals(ClassUtils.PRIMITIVE_TO_BOXED.get(methodType), argumentType)) {
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

        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<Class<?>, Integer> parameterCounts = new HashMap<>();
        for (int i = 1; i < parameters.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.isPrimitive())
                parameterType = ClassUtils.PRIMITIVE_TO_BOXED.get(parameterType);

            int index;
            if (parameterCounts.containsKey(parameterType)) {
                index = parameterCounts.get(parameterType) + 1;
                parameterCounts.put(parameterType, index);
            } else {
                index = 0;
                parameterCounts.put(parameterType, index);
            }
            parameters[i] = context.get(index, parameterType);
        }
        return parameters;
    }

    private static class MethodScore implements Comparable<MethodScore> {

        private final int score;
        private final Method method;

        private MethodScore(int score, Method method) {
            this.score = score;
            this.method = method;
        }

        public int score() {
            return this.score;
        }

        public Method method() {
            return this.method;
        }

        @Override
        public int compareTo(BaseRoseCommand.MethodScore o) {
            return Double.compare(this.score, o.score);
        }

    }

}
