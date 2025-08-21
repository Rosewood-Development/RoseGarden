package dev.rosewood.rosegarden.gui.parameter;

public class Parameter<T> {

    private final String name;
    private final Class<T> type;

    public Parameter(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public Class<T> getType() {
        return this.type;
    }

    public static <T> Context of(Parameter<T> param, T value) {
        return Context.of(param, value);
    }

}
