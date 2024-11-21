package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.utils.NMSUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.OfflinePlayer;
import org.bukkit.Registry;
import org.bukkit.entity.Player;

public final class ArgumentHandlers {

    public static final ArgumentHandler<Boolean> BOOLEAN = new BooleanArgumentHandler();
    public static final ArgumentHandler<org.bukkit.Color> BUKKIT_COLOR = new BukkitColorArgumentHandler();
    public static final ArgumentHandler<Byte> BYTE = new ByteArgumentHandler();
    public static final ArgumentHandler<Character> CHARACTER = new CharacterArgumentHandler();
    public static final ArgumentHandler<Double> DOUBLE = new DoubleArgumentHandler();
    public static final ArgumentHandler<Float> FLOAT = new FloatArgumentHandler();
    public static final ArgumentHandler<String> GREEDY_STRING = new GreedyStringArgumentHandler();
    public static final ArgumentHandler<Integer> INTEGER = new IntegerArgumentHandler();
    public static final ArgumentHandler<java.awt.Color> JAVA_COLOR = new JavaColorArgumentHandler();
    public static final ArgumentHandler<Long> LONG = new LongArgumentHandler();
    public static final ArgumentHandler<OfflinePlayer> OFFLINE_PLAYER = new OfflinePlayerArgumentHandler();
    public static final ArgumentHandler<Player> PLAYER = new PlayerArgumentHandler();
    public static final ArgumentHandler<Player> SELECTOR_PLAYER = new SelectorPlayerArgumentHandler();
    public static final ArgumentHandler<Short> SHORT = new ShortArgumentHandler();
    public static final ArgumentHandler<String> STRING = new StringArgumentHandler();

    private ArgumentHandlers() {

    }

    public static ArgumentHandler<Player> filteredPlayer(Predicate<Player> filter) {
        return new FilteredPlayerArgumentHandler(filter);
    }

    /**
     * Creates an ArgumentHandler for an Enum class.
     *
     * @param enumClass the enum class to create the ArgumentHandler for
     * @return an ArgumentHandler for the specified enum class
     */
    public static <T extends Enum<T>> ArgumentHandler<T> forEnum(Class<T> enumClass) {
        return new EnumArgumentHandler<>(enumClass);
    }

    /**
     * Creates an ArgumentHandler for a Keyed class.
     *
     * @param keyedClass the keyed class to create the ArgumentHandler for
     * @return an ArgumentHandler for the specified keyed class
     * @param <T> The Keyed class to get types of
     * @throws IllegalArgumentException if a registry for the given class does not exist and the class is not an enum
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static <T extends Keyed, Z extends Enum<Z>> ArgumentHandler<T> forKeyed(Class<T> keyedClass) {
        if (keyedClass.isEnum())
            return (ArgumentHandler<T>) (ArgumentHandler<?>) new EnumArgumentHandler<>((Class<Z>) (Class<?>) keyedClass);
        Registry<T> registry = Bukkit.getRegistry(keyedClass);
        if (registry == null)
            throw new IllegalArgumentException("Registry does not exist for " + keyedClass.getName());
        return new RegistryValueArgumentHandler<>(keyedClass, registry);
    }

    @SafeVarargs
    public static <T> ArgumentHandler<T> forValues(Class<T> clazz, T... values) {
        return new ValuesArgumentHandler<>(clazz, Arrays.asList(values));
    }

    public static <T> ArgumentHandler<T> forValues(Class<T> clazz, Collection<T> values) {
        return new ValuesArgumentHandler<>(clazz, new ArrayList<>(values));
    }

}
