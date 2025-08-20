package dev.rosewood.rosegarden.gui.icon;

import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds an {@link AbstractSlotProvider} and the {@link Icon}s that should go in them.
 */
public class IconHolder {

    private final AbstractFillProvider fill;
    private final Icon icon;

    public IconHolder(Icon icon) {
        this.icon = icon;
        this.fill = null;
    }

    public IconHolder(Icon icon, AbstractFillProvider fill) {
        this.icon = icon;
        this.fill = fill;
    }

    /**
     * @return The {@link AbstractSlotProvider} that this holder is holding, or null if it does not exist.
     */
    public AbstractSlotProvider getSlots() {
        return (AbstractSlotProvider) this.icon.getProvider(Providers.SLOT).orElse(null);
    }

    /**
     * @return The {@link Icon} that is this holder is holding.
     *          Modifying this icon will not modify the icon in every slot.
     *          Use {@link IconHolder#getIcons()} to get a {@link List<Icon>} containing all the icons.
     */
    public Icon getIcon() {
        return new Icon(this.icon);
    }

    /**
     * @return Generates a {@link List} of new {@link Icon}s to be placed in the appropriate slots when the menu opens.
     */
    public List<Icon> getIcons() {
        List<Icon> icons = new ArrayList<>();

        // Loop through the slots to get the right amount of items needed.
        for (int ignored : this.getSlots().get(Context.empty()))
            icons.add(new Icon(this.icon));

        return icons;
    }

    /**
     * @return The fill that this holder is holding, or null if it does not exist.
     */
    public AbstractFillProvider getFill() {
        return this.fill;
    }

}
