package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import java.util.function.Supplier;

/* package */ class BackedRoseSetting<T> extends BasicRoseSetting<T> {

    private final RosePlugin backing;
    private T value;

    public BackedRoseSetting(RosePlugin backing, String key, RoseSettingSerializer<T> serializer, T defaultValue, String... comments) {
        this(backing, key, serializer, () -> defaultValue, comments);
    }

    public BackedRoseSetting(RosePlugin backing, String key, RoseSettingSerializer<T> serializer, Supplier<T> defaultValueSupplier, String... comments) {
        super(key, serializer, defaultValueSupplier, comments);
        this.backing = backing;
        this.value = null;
    }

    @Override
    public T get() {
        if (this.value == null)
            this.value = this.backing.getRoseConfig().get(this);
        return this.value;
    }

    @Override
    public boolean isBacked() {
        return true;
    }

    void reload() {
        this.value = null;
    }

}
