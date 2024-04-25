package dev.rosewood.rosegarden.command.framework.handler;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import java.util.Arrays;
import java.util.List;

public class TestArgumentHandler extends ArgumentHandler<String> {

    public static final List<String> SUGGESTIONS = Arrays.asList("apple", "banana", "cherry", "applause", "bandana", "chair");

    public TestArgumentHandler() {
        super(String.class);
    }

    @Override
    public String handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        if (SUGGESTIONS.contains(input))
            return input;

        throw new HandledArgumentException("invalid");
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return SUGGESTIONS;
    }

}
