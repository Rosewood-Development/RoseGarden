package dev.rosewood.rosegarden.codec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * A type token used to preserve generic type information at runtime.
 * <p>
 * Usage:
 * <pre>
 *     SettingType<List<String>> type = new SettingType<List<String>>() {};
 * </pre>
 *
 * @param <T> the represented type
 */
public abstract class SettingType<T> {

    private final Type type;

    protected SettingType() {
        this.type = this.capture();
    }

    protected SettingType(Type type) {
        this.type = Objects.requireNonNull(type, "type");
    }

    private Type capture() {
        Type superClass = this.getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>)
            throw new IllegalStateException("Missing type parameter on SettingType");
        ParameterizedType parameterized = (ParameterizedType) superClass;
        return parameterized.getActualTypeArguments()[0];
    }

    public final Type getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingType<?>)) return false;
        SettingType<?> that = (SettingType<?>) o;
        return this.type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type);
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

}
