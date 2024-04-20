package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValuesArgumentHandler<T> extends ArgumentHandler<T> {

    private final Map<String, T> values;

    protected ValuesArgumentHandler(Class<T> clazz, List<T> values) {
        super(clazz);
        this.values = values.stream().collect(Collectors.toMap(
                Object::toString,
                Function.identity(),
                (t, t2) -> { throw new IllegalStateException("Duplicate toString values not allowed"); },
                () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER))
        );
    }

    @Override
    public T handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        T value = this.values.get(input);
        if (value == null)
            throw new HandledArgumentException("argument-handler-value");
        return value;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return List.copyOf(this.values.keySet());
    }

}
