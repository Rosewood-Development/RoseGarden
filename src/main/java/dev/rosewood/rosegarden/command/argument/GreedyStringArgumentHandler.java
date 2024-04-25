package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GreedyStringArgumentHandler extends ArgumentHandler<String> {

    protected GreedyStringArgumentHandler() {
        super(String.class);
    }

    @Override
    public String handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        List<String> inputs = new ArrayList<>();
        while (inputIterator.hasNext())
            inputs.add(inputIterator.next());

        String combined = String.join(" ", inputs);
        if (combined.isEmpty())
            throw new HandledArgumentException("argument-handler-string");
        return combined;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Collections.singletonList(argument.parameter());
    }

}
