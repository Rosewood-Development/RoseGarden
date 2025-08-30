package dev.rosewood.rosegarden.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.action.Action;
import dev.rosewood.rosegarden.gui.fill.Fill;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.icon.IconHolder;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.animation.MenuTicker;
import dev.rosewood.rosegarden.gui.provider.animation.Tickable;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the view of a menu that a player is currently interacting with.
 */
@SuppressWarnings("deprecation")
public class MenuView implements Tickable {

    private final RosePlugin rosePlugin;
    private final Player player;
    private final RoseMenu menu;
    private final Inventory inventory;
    private final Context initContext;
    private final Map<Integer, Icon> activeIcons;
    private List<Action> tickActions;
    private MenuTicker ticker;
    private String title;
    private boolean closed;

    protected MenuView(RosePlugin rosePlugin, Player player, RoseMenu menu) {
        this(rosePlugin, player, menu, Context.empty());
    }

    protected MenuView(RosePlugin rosePlugin, Player player, RoseMenu menu, Context initContext) {
        this.rosePlugin = rosePlugin;
        this.player = player;
        this.menu = menu;
        this.activeIcons = new HashMap<>();
        this.tickActions = new ArrayList<>();
        this.initContext = initContext;

        AbstractLocaleManager localeManager = rosePlugin.getManager(AbstractLocaleManager.class);
        String rawTitle = localeManager.hasLocaleMessage(menu.getTitle()) ?
                localeManager.getLocaleMessage(menu.getTitle())
                : menu.getTitle();
        Optional<StringPlaceholders> placeholders = this.initContext.get(Parameters.PLACEHOLDERS);
        if (placeholders.isPresent())
            rawTitle = placeholders.get().apply(rawTitle);

        this.title = HexUtils.colorify(PlaceholderAPIHook.applyPlaceholders(player, rawTitle));
        if (menu.getSize() == 5)
            this.inventory = Bukkit.createInventory(null, InventoryType.HOPPER, this.title);
        else
            this.inventory = Bukkit.createInventory(null, menu.getSize(), this.title);

        this.init();
    }

    /**
     * Resets this view from the serialized {@linkplain RoseMenu}.
     */
    public void init() {
        Context context = Context.of(Parameters.PLUGIN, this.rosePlugin)
                .add(Parameters.MENU, this.menu)
                .add(Parameters.VIEW, this)
                .add(Parameters.PLAYER, this.player)
                .addAll(this.initContext);

        // Loop through the serialized menu to add the items.
        for (IconHolder holder : this.menu.getIcons()) {
            if (holder.getFill() != null) {
                Icon icon = holder.getIcon();
                context.add(Parameters.ICON, icon);

                AbstractFillProvider fillProvider = holder.getFill();
                Fill fill = fillProvider.get(context);
                boolean cached = false;
                for (int slot : fill.getSlots(context).get(context)) {
                    if (slot >= this.inventory.getSize())
                        continue;

                    context.add(Parameters.SLOT, slot);
                    AbstractItemProvider itemProvider = fill.getItem(context, slot);

                    RoseItem item = itemProvider.get(context);
                    Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
                    if (!item.isEmpty() && placeholders.isPresent())
                        item.addPlaceholders(placeholders.get());

                    this.inventory.setItem(slot, item.isEmpty() ? null : item.applyPlaceholders(this.player));
                    if (!cached) {
                        this.activeIcons.put(slot, icon);
                        cached = true;
                    }
                }

                continue;
            }

            AbstractSlotProvider slotProvider = holder.getSlots();
            List<Integer> slots = slotProvider.get(context);
            List<Icon> icons = holder.getIcons();

            for (int i = 0; i < slots.size(); i++) {
                int slot = slots.get(i);
                Icon icon = icons.get(i);
                if (slot >= this.inventory.getSize())
                    continue;

                context.add(Parameters.SLOT, slot)
                        .add(Parameters.ICON, icon);
                Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
                if (itemProvider.isPresent()) {
                    RoseItem item = itemProvider.get().get(context);
                    Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
                    if (!item.isEmpty() && placeholders.isPresent())
                        item.addPlaceholders(placeholders.get());

                    this.inventory.setItem(slot, item.isEmpty() ? null : item.applyPlaceholders(this.player));
                } else {
                    this.inventory.setItem(slot, null);
                }

                this.activeIcons.put(slot, icon);
            }
        }
    }

    /**
     * Updates the title of this menu view.
     */
    public void refreshTitle() {
        if (this.player.getOpenInventory().getTopInventory() == this.inventory) {
            AbstractLocaleManager localeManager = this.rosePlugin.getManager(AbstractLocaleManager.class);
            String rawTitle = localeManager.hasLocaleMessage(menu.getTitle()) ? localeManager.getLocaleMessage(menu.getTitle())
                    : menu.getTitle();
            String title = HexUtils.colorify(PlaceholderAPIHook.applyPlaceholders(this.player, rawTitle));
            this.player.getOpenInventory().setTitle(title);
        }
    }

    /**
     * Refreshes the items in the menu.<br>
     * This updates any changes applied to the items.<br>
     * To completely reset the menu, use {@linkplain MenuView#init()}.<br>
     * To refresh one slot, use {@linkplain MenuView#refresh(Context, int)}.
     */
    private void refreshItems() {
        Context context = Context.of(Parameters.PLUGIN, this.rosePlugin)
                .add(Parameters.MENU, this.menu)
                .add(Parameters.VIEW, this)
                .add(Parameters.PLAYER, this.player)
                .addAll(this.initContext);

        for (int slot : this.activeIcons.keySet()) {
            Icon icon = this.activeIcons.get(slot);
            context.add(Parameters.ICON, icon);

            Optional<AbstractFillProvider> fillProvider = icon.getProvider(Providers.FILL);
            if (fillProvider.isPresent()) {
                Fill fill = fillProvider.get().get(context);
                for (int fillSlot : fill.getSlots(context).get(context)) {
                    if (fillSlot >= this.inventory.getSize())
                        continue;

                    context.add(Parameters.SLOT, fillSlot);
                    AbstractItemProvider itemProvider = fill.getItem(context, fillSlot);

                    RoseItem item = itemProvider.get(context);
                    Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
                    if (!item.isEmpty() && placeholders.isPresent())
                        item.addPlaceholders(placeholders.get());

                    this.inventory.setItem(fillSlot, item.isEmpty() ? null : item.applyPlaceholders(this.player));
                }

                continue;
            }

            Optional<AbstractSlotProvider> slotProvider = icon.getProvider(Providers.SLOT);
            if (slotProvider.isEmpty())
                continue;

            List<Integer> slots = slotProvider.get().get(context);
            for (int i = 0; i < slots.size(); i++) {
                context.add(Parameters.SLOT, slot);
                Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
                if (itemProvider.isPresent()) {
                    RoseItem item = itemProvider.get().get(context);
                    ItemStack currentStack = this.inventory.getItem(slot);
                    if (item.isEmpty() && currentStack != null)
                        item = new RoseItem(currentStack).mergeWith(item);

                    Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
                    if (!item.isEmpty() && placeholders.isPresent())
                        item.addPlaceholders(placeholders.get());

                    this.inventory.setItem(slot, item.isEmpty() ? null : item.applyPlaceholders(this.player));
                } else {
                    this.inventory.setItem(slot, null);
                }
            }
        }
    }

    /**
     * Refreshes one slot in the menu.
     * @param context The {@linkplain Context context} to pass to the item.
     * @param slot The slot to refresh.
     */
    public void refresh(Context context, int slot) {
        Icon icon = this.activeIcons.get(slot);
        if (icon == null)
            return;

        Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
        if (itemProvider.isPresent()) {
            RoseItem item = itemProvider.get().get(context);
            ItemStack currentStack = this.inventory.getItem(slot);
            if (item.isEmpty() && currentStack != null)
                item = new RoseItem(currentStack).mergeWith(item);

            Optional<StringPlaceholders> placeholders = context.get(Parameters.PLACEHOLDERS);
            if (!item.isEmpty() && placeholders.isPresent())
                item.addPlaceholders(placeholders.get());

            this.inventory.setItem(slot, item.isEmpty() ? null : item.applyPlaceholders(this.player));
        } else {
            this.inventory.setItem(slot, null);
        }
    }

    /**
     * Refreshes the menu items and title.
     */
    public void refresh() {
        this.refreshTitle();
        this.refreshItems();
    }

    protected void open() {
        if (this.closed)
            throw new IllegalStateException("This MenuView is closed and cannot be reused");
        this.player.openInventory(this.inventory);
    }

    public void close() {
        this.close(false);
    }

    /**
     * Closes the menu for a player.
     * @param transitioning If true, the menu will be forced closed. Set to false if transitioning to a different menu.
     */
    public void close(boolean transitioning) {
        if (this.closed)
            return;

        this.closed = true;
        if (this.ticker != null)
            this.ticker.cancel();

        Context context = Context.of(Parameters.MENU, this.menu)
                .add(Parameters.VIEW, this)
                .add(Parameters.PLAYER, this.player)
                .add(Parameters.PLUGIN, this.rosePlugin)
                .addAll(this.initContext);

        this.menu.call(Providers.ON_CLOSE.getId(), context);

        for (Icon icon : this.activeIcons.values()) {
            Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
            context.add(Parameters.ICON, icon)
                    .add(Parameters.ITEM, itemProvider.map(abstractItemProvider
                            -> abstractItemProvider.get(Context.empty())).orElse(null));

            icon.call(Providers.ON_CLOSE.getId(), context);
        }

        if (!transitioning && this.player.getOpenInventory().getTopInventory().equals(this.inventory))
            this.player.closeInventory();

        this.menu.close(this.player);
    }

    public void setTicking(int speed) {
        if (this.ticker != null)
            this.ticker.cancel();
        this.ticker = new MenuTicker(this.rosePlugin, this, this.player, speed);
    }

    public RoseMenu getMenu() {
        return this.menu;
    }

    public Icon getIcon(int slot) {
        return this.activeIcons.get(slot);
    }

    public Map<Integer, Icon> getActiveIcons() {
        return this.activeIcons;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public MenuTicker getTicker() {
        return this.ticker;
    }

    public List<Action> getTickActions() {
        return this.tickActions;
    }

    public void setTickActions(List<Action> tickActions) {
        this.tickActions = tickActions;
    }

    @Override
    public void run(Context context) {
        for (Action action : this.tickActions)
            action.run(context.addAll(this.initContext));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Context getInitContext() {
        return this.initContext;
    }

}
