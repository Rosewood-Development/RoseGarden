package dev.rosewood.rosegarden.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Provider;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import dev.rosewood.rosegarden.gui.icon.IconHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Handles menu pages and serialization.
 */
public abstract class RoseMenuWrapper {

    protected final RosePlugin rosePlugin;
    private final String id;
    private final File dataFolder;
    private final List<RoseMenu> pages;

    private RoseMenuWrapper(String id, File dataFolder, RosePlugin rosePlugin) {
        this.id = id.toLowerCase();
        this.dataFolder = dataFolder;
        this.rosePlugin = rosePlugin;
        this.pages = new ArrayList<>();
        this.create();
    }

    public RoseMenuWrapper(RosePlugin rosePlugin, String id) {
        this(id.toLowerCase(), rosePlugin.getDataFolder(), rosePlugin);
    }

    /**
     * Creates a menu.
     * Override this to add icons to your menu.
     * This method is mainly for serializing, in-game functionality should be added as {@link Provider}s.
     * Menus will be serialized to a config file in plugin/menus/menu-name.yml
     * These menus can be edited by users.
     */
    public abstract void create();

    /**
     * @param page The page to get, starting at 0.
     * @return A {@link RoseMenu} containing the page.
     */
    public RoseMenu getPage(int page) {
        return this.pages.get(page);
    }

    /**
     * Adds a page to the menu.
     * @param title The title of the page.
     * @param size The size of the page.
     * @param pageConsumer A consumer holding the {@link RoseMenu} of the page.
     */
    public void addPage(String title, int size, Consumer<RoseMenu> pageConsumer) {
        RoseMenu page = new RoseMenu(this, this.getId() + "-" + this.pages.size(), title, size, this.pages.size() + 1);
        this.pages.add(page);
        pageConsumer.accept(page);
    }

    /**
     * Adds a page to the menu.
     * @param title The title of the page.
     * @param size The size of the page.
     * @param pageConsumer A consumer holding the {@link RoseMenu} of the page.
     * @param tickSpeed Values greater than 0 will allow this menu to tick, allowing for things like animations.
     */
    public void addPage(String title, int size, Consumer<RoseMenu> pageConsumer, int tickSpeed) {
        RoseMenu page = new RoseMenu(this, this.getId() + "-" + this.pages.size(), title, size, this.pages.size() + 1, tickSpeed);
        this.pages.add(page);
        pageConsumer.accept(page);
    }

    /**
     * Creates a new {@link RoseMenuWrapper} directly from a given file.
     * @param file The file containing menu information.
     */
    public static RoseMenuWrapper createFromFile(RosePlugin rosePlugin, File file, String id) {
        CommentedFileConfiguration menuConfig = CommentedFileConfiguration.loadConfiguration(file);

        RoseMenuWrapper menu = new RoseMenuWrapper(rosePlugin, id) {
            @Override
            public void create() {
                // Empty
            }
        };

        menu.loadPages(menuConfig);
        return menu;
    }

    /**
     * Writes the entire menu to a file.
     */
    public void register() {
        // Register menus
        File menusDirectory = new File(this.dataFolder, "menus");
        menusDirectory.mkdirs();

        String menuId = this.id;
        File menuConfigFile = new File(menusDirectory, menuId + ".yml");
        boolean exists = menuConfigFile.exists();
        CommentedFileConfiguration menuConfig = CommentedFileConfiguration.loadConfiguration(menuConfigFile);

        AtomicBoolean modified = new AtomicBoolean(false);
        if (!exists) {
            menuConfig.addComments("This file lets you edit everything about the " + menuId + " menu.");
            modified.set(true);
        }

        // Write pages
        this.writePages(menuConfig, modified);

        if (modified.get())
            menuConfig.save(menuConfigFile);

        this.pages.clear();

        // Load menu config values
        Bukkit.getLogger().info("Loading menu: " + menuId + ".yml");
        this.loadPages(menuConfig);
    }

    /**
     * Write the pages of a menu to a file, including info and icons.
     */
    private void writePages(ConfigurationSection section, AtomicBoolean modified) {
        if (!section.contains("pages")) {
            int index = 1;
            for (RoseMenu menu : this.pages) {
                section.set("pages." + index + ".title", menu.getTitle());
                section.set("pages." + index + ".size", menu.getSize());
                if (menu.getTickSpeed() != 0)
                    section.set("pages." + index + ".tick-speed", menu.getTickSpeed());
                this.writeIcons(section.createSection("pages." + index + ".icons"), menu);

                modified.set(true);

                index++;
            }
        }
    }

    /**
     * Write each serializable icon to the file.
     */
    private void writeIcons(ConfigurationSection section, RoseMenu page) {
        int index = 1;
        for (IconHolder holder : page.getIcons()) {
            Icon icon = holder.getIcon();
            ConfigurationSection iconIndexSection = section.createSection(String.valueOf(index));

            for (Provider<?> provider : icon.getProviders().values())
                provider.write(iconIndexSection);

            if (icon.isPersistent())
                iconIndexSection.set("persistent", true);

            if (icon.getEditType() != EditType.NONE) {
                if (icon.getEditType() == EditType.REPLACE) {
                    iconIndexSection.set("editable", true);
                } else {
                    iconIndexSection.set("edit-type", icon.getEditType().toString().toLowerCase());
                }
            }

            index++;
        }
    }

    /**
     * Serializes each page to the file.
     */
    private void loadPages(ConfigurationSection section) {
        if (!section.contains("pages") || !section.isConfigurationSection("pages"))
            return;

        for (String pageId : section.getConfigurationSection("pages").getKeys(false)) {
            String title = section.getString("pages." + pageId + ".title");
            int size = section.getInt("pages." + pageId + ".size");
            int tickSpeed = section.contains("pages." + pageId + ".tick-speed")
                    ? section.getInt("pages." + pageId + ".tick-speed") : 0;

            this.addPage(title, size, (page) -> {
                if (!section.contains("pages." + pageId + ".icons"))
                    return;

                ConfigurationSection iconsSection = section.getConfigurationSection("pages." + pageId + ".icons");
                if (iconsSection == null)
                    return;

                this.loadIcons(iconsSection, page);
            }, tickSpeed);
        }
    }

    /**
     * Serializes each icon in a page to a file.
     */
    private void loadIcons(ConfigurationSection section, RoseMenu page) {
        for (String iconId : section.getKeys(false)) {
            ConfigurationSection iconSection = section.getConfigurationSection(iconId);
            if (iconSection == null)
                continue;

            Icon icon = new Icon();

            if (iconSection.contains("persistent") && iconSection.getBoolean("persistent"))
                icon.markPersistent(true);

            if (iconSection.contains("editable"))
                icon.setEditable(iconSection.getBoolean("editable"));

            if (iconSection.contains("edit-type")) {
                EditType type = EditType.valueOf(iconSection.getString("edit-type").toUpperCase());
                icon.setEditType(type);
            }

            for (Providers.ProviderType<?> providerType : Providers.getRegistry().values()) {
                if (iconSection.contains(providerType.getKey())) {
                    Provider<?> provider = (Provider<?>) providerType.create(iconSection);
                    icon.addProvider(provider);
                }
            }

            Context context = Context.of(Parameters.MENU, page)
                    .add(Parameters.ICON, icon);

            Optional<AbstractFillProvider> fillProvider = icon.getProvider(Providers.FILL);
            if (fillProvider.isPresent()) {
                page.fill(fillProvider.get(), icon);

                continue;
            }

            Optional<AbstractSlotProvider> slotProvider = icon.getProvider(Providers.SLOT);
            if (!slotProvider.isPresent())
                continue;

            page.addIcon(slotProvider.get(), icon);
        }
    }

    /**
     * @return The ID of this menu.
     */
    public String getId() {
        return this.id;
    }

}
