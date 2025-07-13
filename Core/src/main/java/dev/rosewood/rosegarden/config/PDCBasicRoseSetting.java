package dev.rosewood.rosegarden.config;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import org.bukkit.configuration.ConfigurationSection;

class PDCBasicRoseSetting<T> extends BasicRoseSetting<T> implements PDCRoseSetting<T> {

    private final PDCSettingSerializer<T> pdcSerializer;

    protected PDCBasicRoseSetting(PDCSettingSerializer<T> serializer, String key, Supplier<T> defaultValueSupplier, boolean hidden, String... comments) {
        super(serializer, key, defaultValueSupplier, hidden, comments);
        this.pdcSerializer = serializer;
    }

    @Override
    public PDCSettingSerializer<T> getSerializer() {
        return this.pdcSerializer;
    }

    @Override
    public PDCRoseSetting<T> copy(T defaultValue, String... comments) {
        return this.copy(() -> defaultValue, comments);
    }

    @Override
    public PDCRoseSetting<T> copy(Supplier<T> defaultValueSupplier, String... comments) {
        String[] newComments;
        if (comments == null) {
            newComments = new String[0];
        } else {
            newComments = Arrays.copyOf(this.comments, this.comments.length);
        }
        return new PDCBasicRoseSetting<>(this.pdcSerializer, this.key, defaultValueSupplier, false, newComments);
    }

    @Override
    public String toString() {
        return "PDCBasicRoseSetting{" +
                "key='" + this.key + '\'' +
                ", defaultValue=" + this.defaultValueSupplier.get() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PDCBasicRoseSetting)) return false;
        PDCBasicRoseSetting<?> that = (PDCBasicRoseSetting<?>) o;
        return Objects.equals(this.pdcSerializer, that.pdcSerializer) && Objects.equals(this.key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pdcSerializer, this.key);
    }

}
