package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.ArgumentParser;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentHandler;
import dev.rosewood.rosegarden.command.framework.RoseCommandArgumentInfo;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumArgumentHandler<T extends Enum<T>> extends RoseCommandArgumentHandler<T> {

    public EnumArgumentHandler(RosePlugin rosePlugin) {
        super(rosePlugin, null); // This is a special case and will be handled by the preprocessor
    }

    @Override
    protected T handleInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        String input = argumentParser.next();
        T[] enumConstants = this.getHandledType().getEnumConstants();
        Optional<T> value = Stream.of(enumConstants)
                .filter(x -> x.name().equalsIgnoreCase(input))
                .findFirst();

        if (!value.isPresent()) {
            StringPlaceholders placeholders = StringPlaceholders.of(
                    "enum", this.handledType.getSimpleName(),
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
    protected List<String> suggestInternal(RoseCommandArgumentInfo argumentInfo, ArgumentParser argumentParser) {
        argumentParser.next();
        return Stream.of(this.getHandledType().getEnumConstants())
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void preProcess(RoseCommandArgumentInfo argumentInfo) {
        this.handledType = (Class<T>) argumentInfo.getType();
    }

}
