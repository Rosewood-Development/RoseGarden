package dev.rosewood.rosegarden.codec;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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

    @SuppressWarnings("unchecked")
    public final Class<? super T> getRawType() {
        return (Class<? super T>) getRawType(this.type);
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class<?>))
                throw new IllegalArgumentException("Unexpected raw type: " + rawType);
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> rawComponentType = getRawType(componentType);
            return java.lang.reflect.Array.newInstance(rawComponentType, 0).getClass();
        } else if (type instanceof TypeVariable) {
            Type[] bounds = ((TypeVariable<?>) type).getBounds();
            return bounds.length == 0 ? Object.class : getRawType(bounds[0]);
        } else if (type instanceof WildcardType) {
            Type[] bounds = ((WildcardType) type).getUpperBounds();
            return bounds.length == 0 ? Object.class : getRawType(bounds[0]);
        } else {
            throw new IllegalArgumentException("Unable to get raw type for: " + type);
        }
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
