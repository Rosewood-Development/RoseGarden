package dev.rosewood.rosegarden.config;

import dev.rosewood.rosegarden.RosePlugin;
import java.util.function.Supplier;

class BackedRoseSetting<T> extends BasicRoseSetting<T> {

    private final RosePlugin backing;
    private T value;

    protected BackedRoseSetting(RosePlugin backing, SettingSerializer<T> serializer, String key, Supplier<T> defaultValueSupplier, String... comments) {
        super(serializer, key, defaultValueSupplier, false, comments);
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

    @Override
    public String toString() {
        return "BackedRoseSetting{" +
                "key='" + this.key + '\'' +
                ", defaultValue=" + this.defaultValueSupplier.get() +
                ", value=" + this.value +
                '}';
    }

}
