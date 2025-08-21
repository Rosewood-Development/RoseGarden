package dev.rosewood.rosegarden.gui.fill;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Fills {

    private static final Map<String, FillType<?>> REGISTRY = new HashMap<>();

    public static final FillType<MenuFill> MENU = create("menu", MenuFill::new);
    public static final FillType<OutlineFill> OUTLINE = create("outline", OutlineFill::new);
    public static final FillType<CheckeredFill> CHECKERED = create("checkered", CheckeredFill::new);

    public static <T> FillType<T> create(String name, Supplier<T> supplier) {
        FillType<T> fillType = new FillType<>(name, supplier);
        REGISTRY.put(name, fillType);
        return fillType;
    }

    public static Map<String, FillType<?>> getRegistry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    public static class FillType<T> {

        private final String name;
        private final Supplier<T> supplier;

        public FillType(String name, Supplier<T> supplier) {
            this.name = name;
            this.supplier = supplier;
        }

        public T create() {
            return (T) this.supplier.get();
        }

        public String getName() {
            return this.name;
        }

    }

}
