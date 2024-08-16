package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.List;

public class LongArgumentHandler extends ArgumentHandler<Long> {

    protected LongArgumentHandler() {
        super(Long.class);
    }

    @Override
    public Long handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        try {
            return Long.parseLong(input);
        } catch (Exception e) {
            throw new HandledArgumentException("argument-handler-long", StringPlaceholders.of("input", input));
        }
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Collections.singletonList(argument.parameter());
    }

}
