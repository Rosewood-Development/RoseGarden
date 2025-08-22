package dev.rosewood.rosegarden.gui;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.gui.icon.Icon;
import dev.rosewood.rosegarden.gui.icon.IconHolder;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.gui.parameter.Parameters;
import dev.rosewood.rosegarden.gui.provider.Provider;
import dev.rosewood.rosegarden.gui.provider.Providers;
import dev.rosewood.rosegarden.gui.provider.fill.AbstractFillProvider;
import dev.rosewood.rosegarden.gui.provider.slot.AbstractSlotProvider;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.bukkit.configuration.ConfigurationSection;

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
     * Creates a menu.<br>
     * Override this to add icons to your menu.<br>
     * This method is mainly for serializing, in-game functionality should be added as {@linkplain Provider providers}.<br>
     * Menus will be serialized to a config file in plugin/menus/menu-name.yml<br>
     * These menus can be edited by users.<br>
     */
    public abstract void create();

    /**
     * @param page The page to get, starting at 0.
     * @return A {@linkplain RoseMenu} containing the page.
     */
    public RoseMenu getPage(int page) {
        return this.pages.get(page);
    }

    /**
     * Adds a page to the menu.
     *
     * @param title The title of the page.
     * @param size The size of the page.
     * @param pageConsumer A consumer holding the {@linkplain RoseMenu} of the page.
     */
    public void addPage(String title, int size, Consumer<RoseMenu> pageConsumer) {
        RoseMenu page = new RoseMenu(this, this.getId() + "-" + this.pages.size(),
                title, size, this.pages.size() + 1);
        this.pages.add(page);
        pageConsumer.accept(page);
    }

    /**
     * Adds a page to the menu.
     *
     * @param title The title of the page.
     * @param size The size of the page.
     * @param pageConsumer A consumer holding the {@linkplain RoseMenu} of the page.
     * @param tickSpeed Values greater than 0 will allow this menu to tick, allowing for things like animations.
     */
    public void addPage(String title, int size, Consumer<RoseMenu> pageConsumer, int tickSpeed) {
        RoseMenu page = new RoseMenu(this, this.getId() + "-" + this.pages.size(),
                title, size, this.pages.size() + 1, tickSpeed);
        this.pages.add(page);
        pageConsumer.accept(page);
    }

    /**
     * Creates a new {@linkplain RoseMenuWrapper} directly from a given file.
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
        this.rosePlugin.getLogger().info("Loading menu: " + menuId + ".yml");
        this.loadPages(menuConfig);
    }

    /**
     * Write the pages of a menu to a file, including info and icons.
     */
    private void writePages(ConfigurationSection section, AtomicBoolean modified) {
        if (!section.contains("pages")) {
            int index = 1;
            for (RoseMenu menu : this.pages) {
                ConfigurationSection pageSection = section.createSection("pages." + index);

                pageSection.set("title", menu.getTitle());
                pageSection.set("size", menu.getSize());
                if (menu.getTickSpeed() != 0)
                    pageSection.set("tick-speed", menu.getTickSpeed());
                this.writeIconData(pageSection, menu);
                this.writeIcons(pageSection.createSection("icons"), menu);

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
            this.writeIconData(iconIndexSection, icon);

            index++;
        }
    }

    private void writeIconData(ConfigurationSection section, Icon icon) {
        for (Provider<?> provider : icon.getProviders().values())
            provider.write(section);

        if (icon.isPersistent())
            section.set("persistent", true);

        if (icon.getEditType() != EditType.NONE) {
            if (icon.getEditType() == EditType.REPLACE) {
                section.set("editable", true);
            } else {
                section.set("edit-type", icon.getEditType().toString().toLowerCase());
            }
        }
    }

    /**
     * Serializes each page to the file.
     */
    private void loadPages(ConfigurationSection section) {
        if (!section.contains("pages") || !section.isConfigurationSection("pages"))
            return;

        ConfigurationSection pagesSection = section.getConfigurationSection("pages");
        if (pagesSection == null)
            return;

        for (String pageId : pagesSection.getKeys(false)) {
            ConfigurationSection pageSection = pagesSection.getConfigurationSection(pageId);
            if (pageSection == null)
                continue;

            String title = pageSection.getString("title");
            int size = pageSection.getInt("size");
            int tickSpeed = pageSection.getInt("tick-speed", 0);

            this.addPage(title, size, (page) -> {
                this.loadIconData(pageSection, page);

                if (!section.contains("pages." + pageId + ".icons"))
                    return;

                ConfigurationSection iconsSection =
                        section.getConfigurationSection("pages." + pageId + ".icons");
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
            this.loadIconData(iconSection, icon);

            Optional<AbstractFillProvider> fillProvider = icon.getProvider(Providers.FILL);
            if (fillProvider.isPresent()) {
                page.fill(fillProvider.get(), icon);

                continue;
            }

            Optional<AbstractSlotProvider> slotProvider = icon.getProvider(Providers.SLOT);
            if (slotProvider.isEmpty())
                continue;

            page.addIcon(slotProvider.get(), icon);
        }
    }

    private void loadIconData(ConfigurationSection section, Icon icon) {
        if (section.contains("persistent") && section.getBoolean("persistent"))
            icon.markPersistent(true);

        if (section.contains("editable"))
            icon.setEditable(section.getBoolean("editable"));

        if (section.contains("edit-type")) {
            String editTypeStr = section.getString("edit-type");
            if (editTypeStr != null) {
                EditType type = EditType.valueOf(editTypeStr.toUpperCase());
                icon.setEditType(type);
            }
        }

        for (Providers.ProviderType<?> providerType : Providers.getRegistry().values()) {
            if (section.contains(providerType.getKey())) {
                Provider<?> provider = (Provider<?>) providerType.create(section);
                icon.addProvider(provider);
            }
        }
    }

    /**
     * @return The ID of this menu.
     */
    public String getId() {
        return this.id;
    }

}
