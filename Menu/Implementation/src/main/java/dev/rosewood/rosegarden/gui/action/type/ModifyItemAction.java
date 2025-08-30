package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.CompositeItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import java.util.Optional;
import org.bukkit.configuration.ConfigurationSection;

/**
 * An action that modifies the clicked item.<br>
* Usage:<br>
* <pre>
*     {@code
*     trigger-type:
*       '0':
 *         modify-item:
 *           item:
 *             property: value
*     }
* </pre>
 */
public class ModifyItemAction extends AbstractAction {

    public static final String ID = "modify_item";
    public static final String ITEM = "item";

    protected final AbstractItemProvider modifiedItem;

    // Code Constructors

    public ModifyItemAction(AbstractItemProvider modifiedItem) {
        super(ID);

        this.modifiedItem = modifiedItem;
    }

    public ModifyItemAction(Item modifiedItem) {
        super(ID);

        this.modifiedItem = new ItemProvider(modifiedItem);
    }

    public ModifyItemAction(RoseItem modifiedItem) {
        super(ID);

        this.modifiedItem = new ItemProvider(context -> modifiedItem);
    }

    // Config Constructors

    public ModifyItemAction(ConfigurationSection section) {
        super(ID, section);

        this.modifiedItem = new CompositeItemProvider(ITEM, section);
    }

    @Override
    public void write(ConfigurationSection section) {
        // Write the item if it exists.
        if (this.modifiedItem != null)
            this.modifiedItem.write(section);
    }

    @Override
    public void run(Context context) {
        // Merge the current item with the modified item.
        Optional<MenuView> view = context.get(Parameters.VIEW);
        Optional<Icon> icon = context.get(Parameters.ICON);
        if (view.isEmpty() || icon.isEmpty() || this.modifiedItem == null)
            return;

        icon.get().addProvider(this.modifiedItem);
        Optional<Integer> slot = context.get(Parameters.SLOT);
        if (slot.isPresent())
            view.get().refresh(context, slot.get());
        else
            view.get().refresh();
    }

    public AbstractItemProvider getModifiedItem() {
        return this.modifiedItem;
    }

    // Static Constructors

    public static ModifyItemAction of(AbstractItemProvider modifiedItem) {
        return new ModifyItemAction(modifiedItem);
    }

    public static ModifyItemAction of(Item modifiedItem) {
        return new ModifyItemAction(modifiedItem);
    }

    public static ModifyItemAction of(RoseItem modifiedItem) {
        return new ModifyItemAction(modifiedItem);
    }

}
