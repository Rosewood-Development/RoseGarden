package dev.rosewood.rosegarden.gui.item;

import dev.rosewood.rosegarden.utils.HexUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public interface MetaSerializer {

    void read(RoseItem item, ConfigurationSection section);

    void write(RoseItem item, ConfigurationSection section);

    default void invalidateVersion(String meta, String version) {
        Bukkit.getConsoleSender().sendMessage(HexUtils.colorify("&eThe " + meta + " is not available on versions below " + version + "!"));
    }

}
