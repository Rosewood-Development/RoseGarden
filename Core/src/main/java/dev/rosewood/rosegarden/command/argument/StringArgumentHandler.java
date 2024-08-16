package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import java.util.Collections;
import java.util.List;

public class StringArgumentHandler extends ArgumentHandler<String> {

    protected StringArgumentHandler() {
        super(String.class);
    }

    @Override
    public String handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        if (input.trim().isEmpty())
            throw new HandledArgumentException("argument-handler-string");
        return input;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Collections.singletonList(argument.parameter());
    }

}
