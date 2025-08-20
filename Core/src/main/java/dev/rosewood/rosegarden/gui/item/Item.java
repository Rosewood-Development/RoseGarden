package dev.rosewood.rosegarden.gui.item;

import dev.rosewood.rosegarden.gui.parameter.Context;

/**
 * A provider for icons that can be placed in a menu.
 */
@FunctionalInterface
public interface Item {

    RoseItem get(Context context);

}
