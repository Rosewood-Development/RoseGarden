package dev.rosewood.rosegarden.gui.action.type;

import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.action.AbstractAction;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.CompositeItemProvider;
import dev.rosewood.rosegarden.gui.provider.item.ItemProvider;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Optional;

/**
 * An action that modifies the clicked item.
 */
public class ModifyItemAction extends AbstractAction {

    public static final String ID = "modify-item";

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

    // Config Constructor
    public ModifyItemAction(ConfigurationSection section) {
        super(ID, section);

        this.modifiedItem = new CompositeItemProvider(ID, section);
    }

    @Override
    public void write(ConfigurationSection section) {
        // Write the item if it exists.
        if (this.modifiedItem != null)
            this.modifiedItem.write(section.createSection(ID));
    }

    @Override
    public void run(Context context) {
        // Merge the current item with the modified item.
        Optional<MenuView> view = context.get(Parameters.VIEW);
        Optional<Icon> icon = context.get(Parameters.ICON);
        if (!view.isPresent() || !icon.isPresent() || this.modifiedItem == null)
            return;

        //icon.get().addProvider(this.modifiedItem);
        view.get().getIcon(context.get(Parameters.SLOT).get()).addProvider(this.modifiedItem);
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
