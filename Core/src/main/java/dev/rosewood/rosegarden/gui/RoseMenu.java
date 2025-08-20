package dev.rosewood.rosegarden.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.fill.Fill;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.item.Item;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.fill.FillProvider;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import dev.rosewood.rosegarden.gui.provider.slot.SingleSlotProvider;
import dev.rosewood.rosegarden.gui.provider.trigger.TriggerProvider;
import dev.rosewood.rosegarden.gui.icon.IconHolder;
import dev.rosewood.rosegarden.manager.AbstractGuiManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * @param player The player to open the menu for.
     */
    public void open(Player player) {
        this.open(player, Context.empty());
    }

    /**
     * Opens this menu for the given player.
     * @param player The player to open the menu for.
     * @param context {@link Context} to be passed to the menu when it is opened.
     *                               Used for passing custom data to the menu.
     */
    public void open(Player player, Context context) {
        MenuView view = new MenuView(this.rosePlugin, player, this, context);
        if (this.tickSpeed != 0) {
            view.setTicking(this.tickSpeed);

            Optional<TriggerProvider> tick = this.getProvider(Providers.TICK);
            tick.ifPresent(triggerProvider ->
                    view.setTickActions(triggerProvider.get(Context.of(Parameters.MENU, this))));
        }

        this.playerViews.put(player, view);
        view.open();
    }

    /**
     * Adds an {@link Item} to a slot.
     * @param slot The slot to store the item in.
     * @param item The {@link Item} to be placed in the slot.
     * @return The {@link Icon} that will be placed in the slot.
     */
    public Icon addIcon(int slot, Item item) {
        Icon icon = new Icon(item);
        icon.addProvider(new SingleSlotProvider(slot));
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds an {@link AbstractItemProvider} to a slot.
     * @param slot The slot to store the item in.
     * @param item The {@link Item} to be placed in the slot.
     * @return The {@link Icon} that will be placed in the slot.
     */
    public Icon addIcon(int slot, AbstractItemProvider item) {
        Icon icon = new Icon(item);
        icon.addProvider(new SingleSlotProvider(slot));
        IconHolder slotHolder = new IconHolder(icon);

        this.icons.add(slotHolder);
        return icon;
    }

    /**
     * Adds a {@link Item} to an {@link AbstractSlotProvider}.
     * @param slot The {@link AbstractSlotProvider} to store the item in.
     * @param item The {@link Item} to be placed in the slot.
     * @return The {@link Icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, Item item) {
        Icon icon = new Icon(item);
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds a {@link AbstractSlotProvider} to an {@link AbstractSlotProvider}.
     * @param slot The {@link AbstractSlotProvider} to store the item in.
     * @param item The {@link AbstractSlotProvider} to be placed in the slot.
     * @return The {@link Icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, AbstractItemProvider item) {
        Icon icon = new Icon(item);
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Adds a {@link Icon} to an {@link AbstractSlotProvider}.
     * @param slot The {@link AbstractSlotProvider} to store the item in.
     * @param icon The {@link Icon} to be placed in the slot.
     * @return The {@link Icon} that will be placed in the slot.
     */
    public Icon addIcon(AbstractSlotProvider slot, Icon icon) {
        icon.addProvider(slot);
        IconHolder holder = new IconHolder(icon);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Fills the menu with the given fill.
     * @param fill The {@link AbstractFillProvider} that will be used.
     * @return The {@link Icon} that will be placed in the slots.
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
     * @param fill The {@link AbstractFillProvider} that will be used.
     * @return The {@link Icon} that will be placed in the slots.
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
     * @param fill The {@link AbstractFillProvider} that will be used.
     * @return The {@link Icon} that will be placed in the slots.
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
     * @param fill The {@link AbstractFillProvider} that will be used.
     * @return The {@link Icon} that will be placed in the slots.
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
     * @param fill The {@link AbstractFillProvider} to use as a pattern.
     * @param icon The {@link Icon} to be placed in the pattern.
     * @return The {@link Icon} that will be placed in the slots.
     */
    protected Icon fill(AbstractFillProvider fill, Icon icon) {
        icon.addProvider(fill);
        IconHolder holder = new IconHolder(icon, fill);

        this.icons.add(holder);
        return icon;
    }

    /**
     * Closes the menu for the given player.
     * @param player The {@link Player} whose menu should be closed.
     */
    public void close(Player player) {
        MenuView view = this.playerViews.remove(player);
        if (view.getTicker() != null)
            view.getTicker().cancel();

        this.rosePlugin.getManager(AbstractGuiManager.class).close(player);
    }

    /**
     * Grab the {@link Icon} at the given slot for the given player.
     * @param player The {@link Player} who has this menu open.
     * @param slot The slot to get the {@link Icon} of.
     * @return The {@link Icon} that was in the slot.
     *          This may be null if there is no icon in the slot.
     */
    public Icon getIcon(Player player, int slot) {
        MenuView view = this.playerViews.get(player);
        if (view == null)
            return null;

        return view.getActiveIcons().get(slot);
    }

    /**
     * @param player The {@link Player} who has this menu open.
     * @return The open inventory as a Bukkit {@link Inventory}.
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
