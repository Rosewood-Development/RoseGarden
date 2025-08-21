package dev.rosewood.rosegarden.gui.provider.fill;

import dev.rosewood.rosegarden.gui.fill.Fill;
import dev.rosewood.rosegarden.gui.fill.Fills;
import dev.rosewood.rosegarden.gui.fill.MenuFill;
import dev.rosewood.rosegarden.gui.parameter.Context;
import org.bukkit.configuration.ConfigurationSection;

/**
 * A fill that fills the entire menu.
 */
public class FillProvider extends AbstractFillProvider {

    public static final String ID = "fill";

    protected Fill fill;

    // Code Constructors

    public FillProvider(Fill fill) {
        super(ID, null);

        this.fill = fill;
    }

    // Config Constructors

    public FillProvider(String key, ConfigurationSection section) {
        super(ID, section);

        if (section.isBoolean(ID) && section.getBoolean(ID)) {
            this.fill = new MenuFill();
            return;
        }

        if (!section.contains(ID))
            return;

        String fill = section.getString(ID);
        if (fill == null)
            return;

        for (Fills.FillType<?> fillType : Fills.getRegistry().values()) {
            if (fill.equals(fillType.getName())) {
                this.fill = (Fill) fillType.create();
                return;
            }
        }
    }

    @Override
    public void write(ConfigurationSection section) {
        this.fill.write(section);
    }

    @Override
    public Fill get(Context context) {
        return this.fill;
    }

}
