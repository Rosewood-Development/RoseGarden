package dev.rosewood.rosegarden.command.framework;

import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;

public class CommandContext {

    private final CommandSender sender;
    private final String commandLabel;
    private final String[] rawArguments;
    private final Map<Argument.CommandArgument<?>, Object> parametersByArgument;
    private final List<Argument.CommandArgument<?>> usedParameters;
    private final ListMultimap<Class<?>, Object> parametersByType;
    private final Map<String, Object> parametersByName;
    private final List<Argument> argumentsPath;

    public CommandContext(CommandSender sender, String commandLabel, String[] rawArguments) {
        this.sender = sender;
        this.commandLabel = commandLabel;
        this.rawArguments = rawArguments;

        this.parametersByArgument = new LinkedHashMap<>();
        this.usedParameters = new ArrayList<>();
        this.parametersByType = MultimapBuilder.hashKeys().arrayListValues().build();
        this.parametersByName = new LinkedHashMap<>();
        this.argumentsPath = new ArrayList<>();
    }

    /**
     * @return the executor of the command
     */
    public CommandSender getSender() {
        return this.sender;
    }

    /**
     * @return the label of the command being executed
     */
    public String getCommandLabel() {
        return this.commandLabel;
    }

    /**
     * @return the raw unparsed arguments of the command
     */
    public String[] getRawArguments() {
        return Arrays.copyOf(this.rawArguments, this.rawArguments.length);
    }

    /**
     * Puts a command parameter to the context
     *
     * @param argument The argument for the parameter
     * @param value The value
     * @param <T> The type of the value
     */
    protected <T> void put(Argument argument, T value) {
        this.argumentsPath.add(argument);

        if (argument instanceof Argument.NamedProxyArgument)
            argument = ((Argument.NamedProxyArgument) argument).proxy();

        if (argument instanceof Argument.CommandArgument<?>) {
            Argument.CommandArgument<?> commandArgument = (Argument.CommandArgument<?>) argument;
            if (!argument.optional() || value != null)
                this.usedParameters.add(commandArgument);
            this.parametersByArgument.put(commandArgument, value);
            this.parametersByType.put(commandArgument.handler().getHandledType(), value);
            this.parametersByName.put(commandArgument.name(), value);
        }
    }

    /**
     * Gets a command parameter from the context by its type
     * <br>
     * Equivalent to {@code this.get(0, clazz)}
     *
     * @param clazz The class of the value
     * @param <T> The type of the value
     * @return The value, or null if not found
     */
    public <T> T get(Class<T> clazz) {
        return this.get(0, clazz);
    }

    /**
     * Gets a command parameter from the context by its type and index of parameters of the type
     *
     * @param index The index of the parameter for parameters of this type
     * @param clazz The class of the value
     * @param <T> The type of the value
     * @return The value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index, Class<T> clazz) {
        List<Object> values = this.parametersByType.get(clazz);
        if (values.size() <= index)
            return null;
        return (T) this.parametersByType.get(clazz).get(index);
    }

    /**
     * Gets a command parameter from the context by its name
     *
     * @param name The name of the parameter
     * @param <T> The type of the value
     * @return The value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) this.parametersByName.get(name);
    }

    /**
     * Gets a command parameter from the context by its index
     *
     * @param index The index of the parameter
     * @param <T> The type of the value
     * @return The value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) Iterables.get(this.parametersByArgument.values(), index, null);
    }

    /**
     * @return An array of used argument types
     */
    protected Class<?>[] getUsedArgumentTypes() {
        return this.usedParameters.stream()
                .map(Argument.CommandArgument::handler)
                .map(ArgumentHandler::getHandledType)
                .toArray(Class[]::new);
    }

    protected List<Argument> getArgumentsPath() {
        return new ArrayList<>(this.argumentsPath);
    }

}
