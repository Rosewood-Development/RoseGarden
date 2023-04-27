package dev.rosewood.rosegarden.command.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentParser {

    private final CommandContext context;
    private final List<String> arguments;
    private String previous;
    private final Map<Class<?>, Object> contextValues;

    public ArgumentParser(CommandContext context, List<String> arguments) {
        this.context = context;
        this.arguments = arguments;
        this.previous = "";
        this.contextValues = new HashMap<>();
    }

    /**
     * @return the command context
     */
    public CommandContext getContext() {
        return this.context;
    }

    /**
     * @return true if there is another argument available, false otherwise
     */
    public boolean hasNext() {
        return !this.arguments.isEmpty();
    }

    /**
     * @return pops the next available argument, or an empty string if none are available
     */
    public String next() {
        if (!this.hasNext())
            return "";
        this.previous = this.arguments.remove(0);
        return this.previous;
    }

    /**
     * @return the previously returned argument from calling {@link #next()}
     */
    public String previous() {
        return this.previous;
    }

    /**
     * @return peeks the next available argument, or an empty string if none are available
     */
    public String peek() {
        if (!this.hasNext())
            return "";
        return this.arguments.get(0);
    }

    /**
     * Sets a context value that will be available for other handlers using this ArgumentParser
     *
     * @param clazz The class of the value
     * @param value The value
     * @param <T> The type of the value
     */
    public <T> void setContextValue(Class<T> clazz, T value) {
        this.contextValues.put(clazz, value);
    }

    /**
     * Gets a context value set by other handlers using this ArgumentParser
     *
     * @param clazz The class of the value
     * @param <T> The type of the value
     * @return The value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextValue(Class<T> clazz) {
        return (T) this.contextValues.get(clazz);
    }

}
