package dev.rosewood.rosegarden.command.argument;

import java.awt.Color;

public class JavaColorArgumentHandler extends AbstractColorArgumentHandler<Color> {

    protected JavaColorArgumentHandler() {
        super(Color.class);
    }

    @Override
    public Color rgbToColor(int r, int g, int b) {
        return new Color(r, g, b);
    }

}
