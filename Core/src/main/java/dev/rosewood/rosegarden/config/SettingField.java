package dev.rosewood.rosegarden.config;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public final class SettingField<O, T> {

    private final String key;
    private final SettingSerializer<T> settingSerializer;
    private final Function<O, T> getter;
    private final String[] comments;

    public SettingField(String key,
                        SettingSerializer<T> settingSerializer,
                        Function<O, T> getter,
                        String... comments) {
        this.key = key;
        this.settingSerializer = settingSerializer;
        this.getter = getter;
        this.comments = comments;
    }

    public String key() {
        return this.key;
    }

    public SettingSerializer<T> settingSerializer() {
        return this.settingSerializer;
    }

    public Function<O, T> getter() {
        return this.getter;
    }

    public String[] comments() {
        return this.comments;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        SettingField<?, ?> that = (SettingField<?, ?>) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.settingSerializer, that.settingSerializer) &&
                Objects.equals(this.getter, that.getter) &&
                Arrays.equals(this.comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.settingSerializer, this.getter, Arrays.hashCode(this.comments));
    }

    @Override
    public String toString() {
        return "SettingField[" +
                "key=" + this.key + ", " +
                "settingSerializer=" + this.settingSerializer + ", " +
                "getter=" + this.getter + ", " +
                "comments=" + Arrays.toString(this.comments) + ']';
    }

    public static <O, T> SettingField<O, T> of(String key,
                                               SettingSerializer<T> settingSerializer,
                                               Function<O, T> getter,
                                               String... comments) {
        return new SettingField<>(key, settingSerializer, getter, comments);
    }

}
