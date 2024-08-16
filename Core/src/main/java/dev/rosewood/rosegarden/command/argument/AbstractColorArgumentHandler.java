package dev.rosewood.rosegarden.command.argument;

import dev.rosewood.rosegarden.command.framework.Argument;
import dev.rosewood.rosegarden.command.framework.ArgumentHandler;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.InputIterator;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Color;

public abstract class AbstractColorArgumentHandler<T> extends ArgumentHandler<T> {

    private static final Map<String, Color> COLOR_NAME_MAP = new HashMap<>();
    static {
        COLOR_NAME_MAP.put("red", Color.fromRGB(255, 0, 0));
        COLOR_NAME_MAP.put("orange", Color.fromRGB(255, 140, 0));
        COLOR_NAME_MAP.put("yellow", Color.fromRGB(255, 255, 0));
        COLOR_NAME_MAP.put("lime", Color.fromRGB(50, 205, 50));
        COLOR_NAME_MAP.put("green", Color.fromRGB(0, 128, 0));
        COLOR_NAME_MAP.put("blue", Color.fromRGB(0, 0, 255));
        COLOR_NAME_MAP.put("cyan", Color.fromRGB(0, 139, 139));
        COLOR_NAME_MAP.put("light_blue", Color.fromRGB(173, 216, 230));
        COLOR_NAME_MAP.put("purple", Color.fromRGB(138, 43, 226));
        COLOR_NAME_MAP.put("magenta", Color.fromRGB(202, 31, 123));
        COLOR_NAME_MAP.put("pink", Color.fromRGB(255, 182, 193));
        COLOR_NAME_MAP.put("brown", Color.fromRGB(139, 69, 19));
        COLOR_NAME_MAP.put("black", Color.fromRGB(0, 0, 0));
        COLOR_NAME_MAP.put("gray", Color.fromRGB(128, 128, 128));
        COLOR_NAME_MAP.put("light_gray", Color.fromRGB(192, 192, 192));
        COLOR_NAME_MAP.put("white", Color.fromRGB(255, 255, 255));
    }

    protected AbstractColorArgumentHandler(Class<T> clazz) {
        super(clazz);
    }

    protected abstract T rgbToColor(int r, int g, int b);

    @Override
    public final T handle(CommandContext context, Argument argument, InputIterator inputIterator) throws HandledArgumentException {
        String input = inputIterator.next();

        // Try hex values first
        if (input.startsWith("#")) {
            try {
                java.awt.Color color = java.awt.Color.decode(input);
                return this.rgbToColor(color.getRed(), color.getGreen(), color.getBlue());
            } catch (NumberFormatException e) {
                throw new HandledArgumentException("argument-handler-color-hex", StringPlaceholders.of("input", input));
            }
        }

        // Try color names
        Color namedColor = COLOR_NAME_MAP.get(input.toLowerCase());
        if (namedColor != null)
            return this.rgbToColor(namedColor.getRed(), namedColor.getGreen(), namedColor.getBlue());

        // Try RGB
        String input2 = inputIterator.next();
        String input3 = inputIterator.next();

        if (input2.isEmpty() && input3.isEmpty())
            throw new HandledArgumentException("argument-handler-color-hex", StringPlaceholders.of("input", input));

        try {
            return this.rgbToColor(Integer.parseInt(input), Integer.parseInt(input2), Integer.parseInt(input3));
        } catch (NumberFormatException e) {
            throw new HandledArgumentException("argument-handler-color-rgb");
        }
    }

    @Override
    public final List<String> suggest(CommandContext context, Argument argument, String[] args) {
        if (args.length == 0 || args.length == 1) {
            List<String> inputs = new ArrayList<>(Arrays.asList("<#hexCode>", "<0-255> <0-255> <0-255>"));
            inputs.addAll(COLOR_NAME_MAP.keySet());
            return inputs;
        } else if (args.length == 2) {
            return Collections.singletonList("<0-255> <0-255>");
        } else if (args.length == 3) {
            return Collections.singletonList("<0-255>");
        }
        return Collections.emptyList();
    }

}
