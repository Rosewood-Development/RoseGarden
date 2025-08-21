package dev.rosewood.rosegarden.gui.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class Context {

    private final Map<Parameter<?>, Object> params;

    private Context() {
        this.params = new HashMap<>();
    }

    public <T> Optional<T> get(Parameter<T> param) {
        return Optional.ofNullable((T) this.params.get(param));
    }

    public <T, S> Optional<S> getAs(Parameter<T> param, Class<S> clazz) {
        return Optional.ofNullable((T) this.params.get(param)).map(x -> clazz.isAssignableFrom(x.getClass()) ? (S) x : null);
    }

    public <T> Context add(Parameter<T> param, T value) {
        this.params.put(param, value);
        return this;
    }

    public Context addAll(Context context) {
        for (Parameter<?> param : context.params.keySet())
            this.params.put(param, context.params.get(param));

        return this;
    }

    public static <T> Context of(Parameter<T> param, T value) {
        Context context = new Context();
        context.params.put(param, value);
        return context;
    }

    public static <T, U> Context of(Parameter<T> param, T value,
                                 Parameter<U> param1, U value1) {
        Context context = new Context();
        context.params.put(param, value);
        context.params.put(param1, value1);
        return context;
    }

    public static <T, U, V> Context of(Parameter<T> param, T value,
                                 Parameter<U> param1, U value1,
                                 Parameter<V> param2, V value2) {
        Context context = new Context();
        context.params.put(param, value);
        context.params.put(param1, value1);
        context.params.put(param2, value2);
        return context;
    }

    public static <T, U, V, W> Context of(Parameter<T> param, T value,
                                 Parameter<U> param1, U value1,
                                 Parameter<V> param2, V value2,
                                 Parameter<W> param3, W value3) {
        Context context = new Context();
        context.params.put(param, value);
        context.params.put(param1, value1);
        context.params.put(param2, value2);
        context.params.put(param3, value3);
        return context;
    }

    public static Context empty() {
        return new Context();
    }

}
