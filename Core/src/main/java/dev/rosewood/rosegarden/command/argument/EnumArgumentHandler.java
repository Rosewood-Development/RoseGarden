package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumArgumentHandler<T extends Enum<T>> extends ArgumentHandler<T> {

    protected EnumArgumentHandler(Class<T> handledEnumClass) {
        super(handledEnumClass);
    }

    @Override
    public T handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        T[] enumConstants = this.getHandledType().getEnumConstants();
        Optional<T> value = Stream.of(enumConstants)
                .filter(x -> x.name().equalsIgnoreCase(input))
                .findFirst();

        if (value.isEmpty()) {
            StringPlaceholders placeholders = StringPlaceholders.of(
                    "enum", this.getHandledType().getSimpleName(),
                    "input", input,
                    "types", Stream.of(enumConstants).map(x -> x.name().toLowerCase()).collect(Collectors.joining(", "))
            );

            String messageKey;
            if (enumConstants.length <= 10) {
                messageKey = "argument-handler-enum-list";
            } else {
                messageKey = "argument-handler-enum";
            }

            throw new HandledArgumentException(messageKey, placeholders);
        }

        return value.get();
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return Stream.of(this.getHandledType().getEnumConstants())
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

}
