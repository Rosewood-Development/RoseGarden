package dev.rosewood.rosegarden.gui.provider;

import dev.rosewood.rosegarden.gui.provider.animation.FlickerItemProvider;
import dev.rosewood.rosegarden.gui.provider.fill.FillProvider;
import dev.rosewood.rosegarden.gui.provider.item.CompositeItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ConditionalItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.MultiSlotProvider;
import dev.rosewood.rosegarden.gui.provider.slot.SingleSlotProvider;
import dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings({"unchecked"})
public final class Providers {

    private static final Map<String, ProviderType<?>> REGISTRY = new HashMap<>();

    public static final ProviderType<CompositeItemProvider> ITEM = create(ItemProvider.ID, ItemProvider.ID, CompositeItemProvider::new);
    public static final ProviderType<ConditionalItemProvider> REQUIRES_CONDITION = create(ConditionalItemProvider.ID, ItemProvider.ID, ConditionalItemProvider::new);
    public static final ProviderType<FlickerItemProvider> FLICKER = create(FlickerItemProvider.ID, ItemProvider.ID, FlickerItemProvider::new);
    public static final ProviderType<SingleSlotProvider> SLOT = create(SingleSlotProvider.ID, SingleSlotProvider.ID, SingleSlotProvider::new);
    public static final ProviderType<MultiSlotProvider> SLOTS = create(MultiSlotProvider.ID, SingleSlotProvider.ID, MultiSlotProvider::new);
    public static final ProviderType<FillProvider> FILL = create(FillProvider.ID, "fill", FillProvider::new);
    public static final ProviderType<TriggerProvider> RIGHT_CLICK = createClickedTrigger("right-click", "right-click", TriggerProvider::new, ClickType.RIGHT);
    public static final ProviderType<TriggerProvider> LEFT_CLICK = createClickedTrigger("left-click", "left-click", TriggerProvider::new, ClickType.LEFT);
    public static final ProviderType<TriggerProvider> ON_OPEN = create("on-open", "on-open", TriggerProvider::new);
    public static final ProviderType<TriggerProvider> ON_CLOSE = create("on-close", "on-close", TriggerProvider::new);
    public static final ProviderType<TriggerProvider> TICK = create("tick", "tick", TriggerProvider::new);

    private Providers() {

    }

    public static <T> ProviderType<T> create(String name, String type, BiFunction<String, ConfigurationSection, Provider<?>> function) {
        ProviderType<T> providerType = new ProviderType<>(name, type, function,null);
        REGISTRY.put(name, providerType);
        return providerType;
    }

    public static <T> ProviderType<T> createClickedTrigger(String name, String type, BiFunction<String, ConfigurationSection, Provider<?>> function, ClickType click) {
        ProviderType<T> providerType = new ProviderType<>(name, type, function, click);
        REGISTRY.put(name, providerType);
        return providerType;
    }

    public static Map<String, ProviderType<?>> getRegistry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static class ProviderType<T> {

        private final String key;
        private final String type;
        private final BiFunction<String, ConfigurationSection, ? extends Provider<?>> function;
        private final ClickType clickType;

        public ProviderType(String key, String type, BiFunction<String, ConfigurationSection, ? extends Provider<?>> function, ClickType clickType) {
            this.key = key;
            this.type = type;
            this.function = function;
            this.clickType = clickType;
        }

        public T create(ConfigurationSection section) {
            return (T) this.function.apply(this.key, section);
        }

        public String getKey() {
            return this.key;
        }

        public String getType() {
            return this.type;
        }

        public ClickType getClickType() {
            return this.clickType;
        }

    }

}
