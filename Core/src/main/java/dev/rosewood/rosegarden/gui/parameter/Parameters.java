package dev.rosewood.rosegarden.gui.parameter;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.RoseMenu;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.entity.Player;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Parameters {

    public static final Parameter<RosePlugin> PLUGIN = create("plugin", RosePlugin.class);
    public static final Parameter<RoseItem> ITEM = create("item", RoseItem.class);
    public static final Parameter<Icon> ICON = create("icon", Icon.class);
    public static final Parameter<Integer> SLOT = create("slot", Integer.class);
    public static final Parameter<RoseMenu> MENU = create("menu", RoseMenu.class);
    public static final Parameter<MenuView> VIEW = create("view", MenuView.class);
    public static final Parameter<Player> PLAYER = create("player", Player.class);
    public static final Parameter<StringPlaceholders> PLACEHOLDERS = create("placeholders", StringPlaceholders.class);

    public static <T> Parameter<T> create(String name, Class<T> type) {
        return new Parameter<>(name, type);
    }

}
