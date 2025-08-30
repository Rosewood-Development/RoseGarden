package dev.rosewood.rosegarden.gui.provider;

import dev.rosewood.rosegarden.gui.provider.animation.FlickerItemProvider;
import dev.rosewood.rosegarden.gui.provider.fill.FillProvider;
import dev.rosewood.rosegarden.gui.provider.item.ConditionalItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.MultiSlotProvider;
import dev.rosewood.rosegarden.gui.provider.slot.SingleSlotProvider;
import dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;

@SuppressWarnings({"unchecked"})
public final class Providers {

    private static final Map<String, ProviderType<?>> REGISTRY = new HashMap<>();

    public static final ProviderType<ItemProvider> ITEM = create(ItemProvider.ID, ItemProvider.ID, ItemProvider::new);
    public static final ProviderType<ItemProvider> CONDITIONAL_ITEM = create(ConditionalItemProvider.ID, ItemProvider.ID, ConditionalItemProvider::new);
    public static final ProviderType<ItemProvider> FLICKER = create(FlickerItemProvider.ID, ItemProvider.ID, FlickerItemProvider::new);
    public static final ProviderType<SingleSlotProvider> SLOT = create(SingleSlotProvider.ID, SingleSlotProvider.ID, SingleSlotProvider::new);
    public static final ProviderType<MultiSlotProvider> SLOTS = create(MultiSlotProvider.ID, SingleSlotProvider.ID, MultiSlotProvider::new);
    public static final ProviderType<FillProvider> FILL = create(FillProvider.ID, "fill", FillProvider::new);
    public static final ProviderType<TriggerProvider> RIGHT_CLICK = createClickedTrigger("right-click", "right-click", TriggerProvider::new, ClickType.RIGHT);
    public static final ProviderType<TriggerProvider> LEFT_CLICK = createClickedTrigger("left-click", "left-click", TriggerProvider::new, ClickType.LEFT);
    public static final ProviderType<TriggerProvider> ON_OPEN = create("open", "open", TriggerProvider::new);
    public static final ProviderType<TriggerProvider> ON_CLOSE = create("close", "close", TriggerProvider::new);
    public static final ProviderType<TriggerProvider> TICK = create("tick", "tick", TriggerProvider::new);

    private Providers() {

    }

    /**
     * Creates and registers a new {@linkplain ProviderType provider type} which can be attached to an {@linkplain dev.rosewood.rosegarden.gui.icon.Icon icon}.
     *
     * @param id The id of the provider, used directly in menu configuration files.
     * @param group The group of provider, used for overwriting any existing providers so that an icon can not have multiple providers of the same group.
     * @param function The constructor of a {@linkplain AbstractProvider provider}.
     * @return The created {@linkplain ProviderType provider type}.
     * @param <T> The class extending {@linkplain AbstractProvider}.
     */
    public static <T> ProviderType<T> create(String id, String group, BiFunction<String, ConfigurationSection, Provider<?>> function) {
        ProviderType<T> providerType = new ProviderType<>(id, group, function,null);
        REGISTRY.put(id, providerType);
        return providerType;
    }

    /**
     * Creates and registers a new {@linkplain ProviderType provider type},
     * with a {@linkplain ClickType click type},
     * which can be attached to an {@linkplain dev.rosewood.rosegarden.gui.icon.Icon icon}.
     *
     * @param id The id of the provider, used directly in menu configuration files.
     * @param group The group of provider, used for overwriting any existing providers so that an icon can not have multiple providers of the same group.
     * @param function The constructor of a {@linkplain AbstractProvider provider}.
     * @param click The {@linkplain ClickType click type} to use.
     * @return The created {@linkplain ProviderType provider type}.
     * @param <T> The class extending {@linkplain AbstractProvider}.
     */
    public static <T> ProviderType<T> createClickedTrigger(String id, String group, BiFunction<String, ConfigurationSection, Provider<?>> function, ClickType click) {
        ProviderType<T> providerType = new ProviderType<>(id, group, function, click);
        REGISTRY.put(id, providerType);
        return providerType;
    }

    public static Map<String, ProviderType<?>> getRegistry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static class ProviderType<T> {

        private final String id;
        private final String group;
        private final BiFunction<String, ConfigurationSection, ? extends Provider<?>> function;
        private final ClickType clickType;

        public ProviderType(String id, String group, BiFunction<String, ConfigurationSection, ? extends Provider<?>> function, ClickType clickType) {
            this.id = id;
            this.group = group;
            this.function = function;
            this.clickType = clickType;
        }

        public T create(ConfigurationSection section) {
            return (T) this.function.apply(this.id, section);
        }

        public String getId() {
            return this.id;
        }

        public String getGroup() {
            return this.group;
        }

        public ClickType getClickType() {
            return this.clickType;
        }

    }

}
