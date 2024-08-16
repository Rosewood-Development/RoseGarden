package dev.rosewood.rosegarden.command.framework;

import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;

public abstract class ArgumentHandler<T> {

    private final Class<T> handledType;

    public ArgumentHandler(Class<T> handledType) {
        this.handledType = handledType;
    }

    /**
     * Converts a String input from an argument instance into the handled type.
     * The handler must consume at least one element from the inputIterator else the handle will fail.
     *
     * @param context A readonly command context
     * @param argument The argument being handled
     * @param inputIterator The input iterator
     * @return The String input converted to the handled object type, or null if the conversion failed
     * @throws HandledArgumentException when an argument is unable to be handled
     */
    public abstract T handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException;

    /**
     * Gets command argument suggestions for the remaining player input.
     *
     * @param context A readonly command context
     * @param argument The argument being handled
     * @param args The player input for this argument
     * @return A List of possible argument suggestions
     */
    public abstract List<String> suggest(CommandContext context, Argument argument, String[] args);

    /**
     * @return the Class that this argument handler handles
     */
    public Class<T> getHandledType() {
        return this.handledType;
    }

    /**
     * Thrown when an argument has an issue while parsing, the exception message is the reason why the argument failed to parse
     */
    public static class HandledArgumentException extends Exception {

        private final StringPlaceholders placeholders;

        public HandledArgumentException(String message, StringPlaceholders placeholders) {
            super(message);
            this.placeholders = placeholders;
        }

        public HandledArgumentException(String message) {
            this(message, StringPlaceholders.empty());
        }

        public StringPlaceholders getPlaceholders() {
            return this.placeholders;
        }

    }

}
