package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.gui.EditType;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.MenuView;
import dev.rosewood.rosegarden.gui.RoseMenu;
import dev.rosewood.rosegarden.gui.RoseMenuWrapper;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.persistence.InventoryContainerType;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.item.AbstractItemProvider;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.persistence.PersistentDataContainer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AbstractGuiManager extends Manager implements Listener {

    private final Map<String, RoseMenuWrapper> menus;
    private final Map<Player, RoseMenu> openMenus;
    private final InventoryContainerType inventoryContainerType;

    public AbstractGuiManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.menus = new HashMap<>();
        this.openMenus = new HashMap<>();
        this.inventoryContainerType = new InventoryContainerType(rosePlugin);

        Bukkit.getPluginManager().registerEvents(this, rosePlugin);
    }

    /**
     * Opens the given menu at page 1 for the player.
     * @param id The ID of the menu to open.
     * @param player The {@link Player} who should see the menu.
     */
    public void open(String id, Player player) {
        this.open(id.toLowerCase(), player, 1);
    }

    /**
     * Opens the given menu at page 1 for the player.
     * @param id The ID of the menu to open.
     * @param player The {@link Player} who should see the menu.
     * @param context The {@link Context} to open the menu with, used for passing custom data.
     */
    public void open(String id, Player player, Context context) {
        this.open(id.toLowerCase(), player, 1, context);
    }

    /**
     * Opens the given menu at the given page for the player.
     * @param id The ID of the menu to open.
     * @param player The {@link Player} who should see the menu.
     * @param page The page of the menu that should be opened. Pages start at index 1.
     */
    public void open(String id, Player player, int page) {
        this.open(id, player, page, Context.empty());
    }

    /**
     * Opens the given menu at page 1 for the player.
     * @param id The ID of the menu to open.
     * @param player The {@link Player} who should see the menu.
     * @param page The page of the menu that should be opened. Pages start at index 1.
     * @param context The {@link Context} to open the menu with, used for passing custom data.
     */
    public void open(String id, Player player, int page, Context context) {
        RoseMenuWrapper wrapper = this.menus.get(id.toLowerCase());
        if (wrapper == null)
            return;

        RoseMenu menu = wrapper.getPage(page - 1);
        context.add(Parameters.MENU, menu);

        this.openMenus.put(player, menu);
        menu.open(player, context);
    }

    /**
     * Closes the menu for a player.
     * @param player The player to close the menu for.
     */
    public void close(Player player) {
        player.closeInventory();
        this.openMenus.remove(player);
    }

    /**
     * Called before registering menus.
     * Used for registering custom {@link Providers}, {@link dev.rosewood.rosegarden.gui.action.Actions}, etc.
     */
    protected abstract void setup();

    @Override
    public void reload() {
        this.setup();

        this.getMenus().stream()
                .map(x -> x.apply(this.rosePlugin))
                .forEach(x -> this.menus.put(x.getId().toLowerCase(), x));

        this.menus.values().forEach(RoseMenuWrapper::register);


        this.copyPreMadeMenus();
        // Load pre-made menus if they don't already exist.
        File menusFolder = new File(this.rosePlugin.getDataFolder(), "menus");
        if (!menusFolder.exists())
            return;

        this.loadMenusFromFolder(menusFolder);
    }

    private void loadMenusFromFolder(File folder) {
        try {
            Files.walk(folder.toPath()).filter(Files::isRegularFile).forEach(path -> {
                File file = path.toFile();

                String parent = file.getParentFile().getName().equalsIgnoreCase("menus") ? "" : file.getParentFile().getName() + "/";
                String id = (parent + file.getName().replace(".yml", "")).toLowerCase();
                if (this.menus.containsKey(id))
                    return;

                RoseMenuWrapper menu = RoseMenuWrapper.createFromFile(this.rosePlugin, file, id);
                this.menus.put(id, menu);
            });
        } catch (IOException ignored) {
        }
    }

    private void copyPreMadeMenus() {
        try {
            URL url = AbstractGuiManager.class.getClassLoader().getResource("menus");
            if (url == null)
                return;

            URLConnection connection = url.openConnection();
            if (!(connection instanceof JarURLConnection))
                return;

            JarURLConnection jarConnection = (JarURLConnection) connection;
            JarFile jarFile = jarConnection.getJarFile();
            String prefix = jarConnection.getEntryName() + "/";

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (!name.startsWith(prefix) || !name.endsWith(".yml"))
                    continue;

                InputStream inputStream = jarFile.getInputStream(entry);

                File file = new File(this.rosePlugin.getDataFolder(), name);
                FileUtils.copyInputStreamToFile(inputStream, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {
        this.menus.clear();
    }

    /**
     * @param player The {@link Player} to use.
     * @return The menu that is open for this player.
     */
    public RoseMenu getOpenMenu(Player player) {
        return this.openMenus.get(player);
    }

    /**
     * @return The menus that are currently open by all players.
     */
    public Map<Player, RoseMenu> getOpenMenus() {
        return this.openMenus;
    }

    /**
     * @return A list of {@link RoseMenuWrapper} menus to be used by this plugin.
     */
    public abstract List<Function<RosePlugin, RoseMenuWrapper>> getMenus();

    /**
     * @return The menus currently loaded by the plugin.
     */
    public Map<String, RoseMenuWrapper> getActiveMenus() {
        return Collections.unmodifiableMap(this.menus);
    }

    /**
     * Manages interacting with the open menu.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        AbstractGuiManager guiManager = this.rosePlugin.getManager(AbstractGuiManager.class);
        RoseMenu menu = guiManager.getOpenMenu(player);
        if (menu == null || menu.asInventory(player) == null || event.getClickedInventory() == null)
            return;

        if (event.getClickedInventory() == player.getInventory() && event.getClick().isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        if (!menu.asInventory(player).equals(event.getClickedInventory()))
            return;

        MenuView view = menu.getView(player);
        if (view == null)
            return;

        Icon icon = view.getIcon(event.getSlot());
        if (icon == null) {
            event.setCancelled(true);
            return;
        }

        if (icon.getEditType() == EditType.NONE)
            event.setCancelled(true);

        ClickType clickType = event.getClick();
        String trigger = this.getTrigger(clickType);
        if (trigger == null)
            return;

        Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
        Context context = Context.of(Parameters.ICON, icon)
                .add(Parameters.SLOT, event.getSlot())
                .add(Parameters.ITEM, itemProvider.map(abstractItemProvider -> abstractItemProvider.get(Context.empty())).orElse(null))
                .add(Parameters.MENU, menu)
                .add(Parameters.VIEW, view)
                .add(Parameters.PLAYER, player)
                .add(Parameters.PLUGIN, this.rosePlugin)
                .addAll(menu.getView(player).getInitContext());
        icon.call(trigger, context);

        if (icon.isPersistent()) {
            NamespacedKey key = new NamespacedKey(this.rosePlugin, menu.getId());
            PersistentDataContainer pdc = player.getPersistentDataContainer();

            if (!pdc.has(key))
                pdc.set(key, this.inventoryContainerType, new HashMap<>());

            RoseItem item = itemProvider.isPresent() ? new RoseItem(menu.asInventory(player).getItem(event.getSlot())) : RoseItem.empty();
            Map<Integer, RoseItem> persistentItems = pdc.get(key, this.inventoryContainerType);
            if (persistentItems == null)
                return;

            persistentItems.put(event.getSlot(), item);
            pdc.set(key, this.inventoryContainerType, persistentItems);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        AbstractGuiManager guiManager = this.rosePlugin.getManager(AbstractGuiManager.class);
        RoseMenu menu = guiManager.getOpenMenu(player);
        if (menu == null || menu.asInventory(player) == null)
            return;

        if (!menu.asInventory(player).equals(event.getInventory()))
            return;

        MenuView view = menu.getView(player);
        if (view == null)
            return;

        for (int slot : event.getInventorySlots()) {
            Icon icon = view.getIcon(slot);
            if (icon == null) {
                event.setCancelled(true);
                return;
            }

            if (icon.getEditType() == EditType.NONE)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        AbstractGuiManager guiManager = this.rosePlugin.getManager(AbstractGuiManager.class);
        RoseMenu menu = guiManager.getOpenMenu(player);

        if (menu == null || menu.asInventory(player) == null)
            return;

        if (!menu.asInventory(player).equals(event.getInventory()))
            return;

        Context context = Context.of(Parameters.MENU, menu)
                .add(Parameters.VIEW, menu.getView(player))
                .add(Parameters.PLAYER, player)
                .add(Parameters.PLUGIN, this.rosePlugin)
                .addAll(menu.getView(player).getInitContext());

        menu.call(Providers.ON_OPEN.getKey(), context);

        for (Icon icon : menu.getView(player).getActiveIcons().values()) {
            Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
            context.add(Parameters.ICON, icon)
                    .add(Parameters.ITEM, itemProvider.map(abstractItemProvider -> abstractItemProvider.get(Context.empty())).orElse(null));

            icon.call(Providers.ON_OPEN.getKey(), context);

            if (icon.isPersistent()) {
                NamespacedKey key = new NamespacedKey(this.rosePlugin, menu.getId());
                PersistentDataContainer pdc = player.getPersistentDataContainer();

                if (!pdc.has(key))
                    return;

                Map<Integer, RoseItem> persistentItems = pdc.get(key, this.inventoryContainerType);
                if (persistentItems == null)
                    return;

                for (int i : persistentItems.keySet()) {
                    RoseItem item = persistentItems.get(i);
                    if (item.isEmpty())
                        continue;

                    menu.asInventory(player).setItem(i, item.asItemStack());
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        AbstractGuiManager guiManager = this.rosePlugin.getManager(AbstractGuiManager.class);
        RoseMenu menu = guiManager.getOpenMenu(player);
        if (menu == null || menu.asInventory(player) == null)
            return;

        if (!menu.asInventory(player).equals(event.getInventory()))
            return;

        Context context = Context.of(Parameters.MENU, menu)
                .add(Parameters.VIEW, menu.getView(player))
                .add(Parameters.PLAYER, player)
                .add(Parameters.PLUGIN, this.rosePlugin)
                .addAll(menu.getView(player).getInitContext());

        menu.call(Providers.ON_CLOSE.getKey(), context);

        for (Icon icon : menu.getView(player).getActiveIcons().values()) {
            Optional<AbstractItemProvider> itemProvider = icon.getProvider(Providers.ITEM);
            context.add(Parameters.ICON, icon)
                    .add(Parameters.ITEM, itemProvider.map(abstractItemProvider -> abstractItemProvider.get(Context.empty())).orElse(null));

            icon.call(Providers.ON_CLOSE.getKey(), context);
        }

        menu.getView(player).close();
    }

    private String getTrigger(ClickType clickType) {
        for (Providers.ProviderType<?> providerType : Providers.getRegistry().values()) {
            if (providerType.getClickType() == clickType)
                return providerType.getKey();
        }

        return null;
    }

}
