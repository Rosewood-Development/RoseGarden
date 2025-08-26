package dev.rosewood.rosegarden.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.fill.AreaFill;
import dev.rosewood.rosegarden.gui.fill.Fill;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.icon.IconHolder;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.fill.FillProvider;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import dev.rosewood.rosegarden.gui.provider.slot.SingleSlotProvider;
import dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.ApiStatus;

/**
 * A menu, or menu page, that stores icons.
 */
public class RoseMenu extends Icon {

    private final RoseMenuWrapper wrapper;
    private final RosePlugin rosePlugin;
    private final String id;
    private final List<IconHolder> icons;
    private final Map<Player, MenuView> playerViews;
    private final int pageIndex;
    private String title;
    private int size;
    private int tickSpeed;

    protected RoseMenu(RoseMenuWrapper wrapper, String id, String title, int size, int pageIndex) {
        this.wrapper = wrapper;
        this.rosePlugin = wrapper.rosePlugin;
        this.id = id;
        this.title = title;
        this.size = MenuUtils.roundSize(size);
        this.icons = new ArrayList<>();
        this.playerViews = new HashMap<>();
        this.pageIndex = pageIndex;
    }

    protected RoseMenu(RoseMenuWrapper wrapper, String id, String title, int size, int pageIndex, int tickSpeed) {
        this(wrapper, id, title, size, pageIndex);

        this.tickSpeed = tickSpeed;
    }

    /**
     * Opens this menu for the given player.
     *
     * @param player The player to open the menu for.
     */
    public MenuView open(Player player) {
        return this.open(player, Context.empty());
    }

    /**
     * Opens this menu for the given player.
     *
     * @param player The player to open the menu for.
     * @param context {@linkplain Context context} to be passed to the menu when it is opened.
     *                               Used for passing custom data to the menu.
     * @return The opened MenuView
     */
    public MenuView open(Player player, Context context) {
        return this.rosePlugin.getManager(AbstractGuiManager.class).open(this.id, player, context);
    }

    @ApiStatus.Internal
    public MenuView handleOpen(Player player, Context context) {
        MenuView view = new MenuView(this.rosePlugin, player, this, context);
        if (this.tickSpeed != 0) {
            view.setTicking(this.tickSpeed);

            Optional<TriggerProvider> tick = this.getProvider(Providers.TICK);
            tick.ifPresent(triggerProvider ->
                    view.setTickActions(triggerProvider.get(context)));
        }

        this.playerViews.put(player, view);
        view.open();
        return view;
    }

    /**
     * Adds an {@linkplain Item item} to a slot.
     *
     * @param slot The slot to store the item in.
     * @param item The {@linkplain Item item} to be placed in the slot.
     * @return The {@linkplain Icon icon} that will be placed in the slot.
     */
    public Icon addIcon(int slot, Item item) {
        Icon icon = new Icon(item);
        icon.addProvider(new SingleSlotProvider(slot));
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds an {@linkplain AbstractItemProvider item provider} to a slot.
     *
     * @param slot The slot to store the item in.
     * @param item The {@linkplain AbstractItemProvider item provider} to be placed in the slot.
     * @return The {@linkplain Icon icon} that will be placed in the slot.
     */
    public Icon addIcon(int slot, AbstractItemProvider item) {
        Icon icon = new Icon(item);
        icon.addProvider(new SingleSlotProvider(slot));
        IconHolder slotHolder = new IconHolder(icon);

        this.icons.add(slotHolder);
        return icon;
    }

    /**
     * Adds an {@linkplain Item item} to a {@linkplain AbstractSlotProvider slot provider}.
     *
     * @param slot The {@linkplain AbstractSlotProvider slot provider} to store the item in.
     * @param item The {@linkplain Item item} to be placed in the slot.
     * @return The {@linkplain Icon icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, Item item) {
        Icon icon = new Icon(item);
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds an {@linkplain AbstractItemProvider item provider} to a {@linkplain AbstractSlotProvider slot provider}.
     *
     * @param slot The {@linkplain AbstractSlotProvider slot provider} to store the item in.
     * @param item The {@linkplain AbstractItemProvider item provider} to be placed in the slot.
     * @return The {@linkplain Icon icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, AbstractItemProvider item) {
        Icon icon = new Icon(item);
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds an {@linkplain Icon icon} to a {@linkplain AbstractSlotProvider slot provider}.
     *
     * @param slot The {@linkplain AbstractSlotProvider slot provider} to store the item in.
     * @param icon The {@linkplain Icon icon} to be placed in the slot.
     * @return The {@linkplain Icon icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, Icon icon) {
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Automatically fills the menu with an {@linkplain AreaFill}.
     *
     * @param item The {@linkplain Item item} to be placed in the slots.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    public Icon fill(Item item) {
        return this.fill(new AreaFill(), item);
    }

    /**
     * Fills the menu with the given fill.
     *
     * @param fill The {@linkplain Fill fill} to use as a pattern.
     * @param item The {@linkplain Item item} to be placed in the slots.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    public Icon fill(Fill fill, Item item) {
        Icon icon = new Icon(item);
        FillProvider provider = new FillProvider(fill);
        icon.addProvider(provider);
        IconHolder holder = new IconHolder(icon, provider);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Fills the menu with the given fill.
     *
     * @param fill The {@linkplain Fill fill} to use as a pattern.
     * @param item The {@linkplain AbstractItemProvider item provider} to be placed in the slots.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    public Icon fill(Fill fill, AbstractItemProvider item) {
        Icon icon = new Icon(item);
        FillProvider provider = new FillProvider(fill);
        icon.addProvider(provider);
        IconHolder holder = new IconHolder(icon, provider);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Fills the menu with the given fill.
     *
     * @param fill The {@linkplain Fill fill} to use as a pattern.
     * @param item The {@linkplain Item item} to be placed in the slots.
     * @param slots The {@linkplain AbstractSlotProvider slot provider} to fill.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    public Icon fill(Fill fill, Item item, AbstractSlotProvider slots) {
        Icon icon = new Icon(item);
        FillProvider provider = new FillProvider(fill);
        icon.addProvider(provider);
        icon.addProvider(slots);
        IconHolder holder = new IconHolder(icon, provider);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Fills the menu with the given fill.
     *
     * @param fill The {@linkplain Fill fill} that will be used.
     * @param item The {@linkplain AbstractItemProvider item provider} to be placed in the slots.
     * @param slots The {@linkplain AbstractSlotProvider slot provider} to fill.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    public Icon fill(Fill fill, AbstractItemProvider item, AbstractSlotProvider slots) {
        Icon icon = new Icon(item);
        FillProvider provider = new FillProvider(fill);
        icon.addProvider(provider);
        icon.addProvider(slots);
        IconHolder holder = new IconHolder(icon, provider);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Fills the menu with the given icon.
     *
     * @param fill The {@linkplain AbstractFillProvider fill provider} to use as a pattern.
     * @param icon The {@linkplain Icon icon} to be placed in the pattern.
     * @return The {@linkplain Icon icon} that will be placed in the slots.
     */
    protected Icon fill(AbstractFillProvider fill, Icon icon) {
        icon.addProvider(fill);
        IconHolder holder = new IconHolder(icon, fill);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Handles the menu closing for the given player.
     *
     * @param player The {@linkplain Player player} whose menu is being closed.
     */
    public void close(Player player) {
        MenuView view = this.playerViews.remove(player);
        if (view != null)
            view.close();
        this.rosePlugin.getManager(AbstractGuiManager.class).handleClose(this, player);
    }

    /**
     * Grab the {@linkplain Icon icon} at the given slot for the given player.
     *
     * @param player The {@linkplain Player player} who has this menu open.
     * @param slot The slot to get the {@linkplain Icon icon} of.
     * @return The {@linkplain Icon icon} that was in the slot.
     *          This may be null if there is no icon in the slot.
     */
    public Icon getIcon(Player player, int slot) {
        MenuView view = this.playerViews.get(player);
        if (view == null)
            return null;

        return view.getActiveIcons().get(slot);
    }

    /**
     * @param player The {@linkplain Player player} who has this menu open.
     * @return The open inventory as a Bukkit {@linkplain Inventory inventory}.
     */
    public Inventory asInventory(Player player) {
        MenuView view = this.playerViews.get(player);
        if (view == null)
            return null;

        return view.getInventory();
    }

    public RoseMenuWrapper getWrapper() {
        return this.wrapper;
    }

    public MenuView getView(Player player) {
        return this.playerViews.get(player);
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSize() {
        return this.size;
    }

    public List<IconHolder> getIcons() {
        return this.icons;
    }

    public int getTickSpeed() {
        return tickSpeed;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

}
