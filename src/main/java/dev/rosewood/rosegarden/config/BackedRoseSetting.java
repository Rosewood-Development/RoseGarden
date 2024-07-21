package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import java.util.function.Supplier;

/* package */ class BackedRoseSetting<T> extends BasicRoseSetting<T> {

    private final RosePlugin backing;

    public BackedRoseSetting(RosePlugin backing, String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        this(backing, key, serializer, () -> defaultValue, comments);
    }

    public BackedRoseSetting(RosePlugin backing, String key, RoseSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        super(key, serializer, defaultValueSupplier, comments);
        this.backing = backing;
    }

    @Override
    public T get() {
        return this.backing.getRoseConfig().get(this);
    }

}
