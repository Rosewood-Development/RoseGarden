package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

public class RegistryValueArgumentHandler<T extends Keyed> extends ArgumentHandler<T> {

    private final Registry<T> registry;

    public RegistryValueArgumentHandler(Class<T> handledType, Registry<T> registry) {
        super(handledType);
        this.registry = registry;
    }

    @Override
    public T handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();
        NamespacedKey key = NamespacedKey.fromString(input.toLowerCase());

        StringPlaceholders placeholders = StringPlaceholders.of(
                "type", this.getHandledType().getSimpleName(),
                "input", input
        );

        if (key == null)
            throw new HandledArgumentException("argument-handler-registry-value", placeholders);

        T value = this.registry.get(key);
        if (value == null)
            throw new HandledArgumentException("argument-handler-registry-value", placeholders);

        return value;
    }

    @Override
    public List<String> suggest(CommandContext context, Argument argument, String[] args) {
        return this.registry.stream()
                .map(x -> asMinimalString(x.getKey()))
                .collect(Collectors.toList());
    }

    private static String asMinimalString(NamespacedKey key) {
        if (key.getNamespace().equals(NamespacedKey.MINECRAFT))
            return key.getKey();
        return key.toString();
    }

}
