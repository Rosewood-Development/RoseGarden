package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;

public class BooleanArgumentHandler extends ArgumentHandler<Boolean> {

    protected BooleanArgumentHandler() {
        super(Boolean.class);
    }

    @Override
    public Boolean handle(CommandContext context, Argument argument, InputIterator inputIterator) {
        String input = inputIterator.next();
        return switch (input.toLowerCase()) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new HandledArgumentException("argument-handler-boolean", StringPlaceholders.of("input", input));
        };
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return List.of("true", "false");
    }

}
