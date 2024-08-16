package dev.rosewood.rosegarden.command.argument;

import org.bukkit.Color;

public class BukkitColorArgumentHandler extends AbstractColorArgumentHandler<Color> {

    protected BukkitColorArgumentHandler() {
        super(Color.class);
    }

    @Override
    protected Color rgbToColor(int r, int g, int b) {
        return Color.fromRGB(r, g, b);
    }

}
