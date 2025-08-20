package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class CompassMetaSerializer implements MetaSerializer {

    public static final String COMPASS = "compass";
    public static final String LODESTONE_LOCATION = COMPASS + ".lodestone-location";
    public static final String WORLD = LODESTONE_LOCATION + ".world";
    public static final String X = LODESTONE_LOCATION + ".x";
    public static final String Y = LODESTONE_LOCATION + ".y";
    public static final String Z = LODESTONE_LOCATION + ".z";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(COMPASS))
            return;

        if (section.contains(LODESTONE_LOCATION)) {
            ConfigurationSection locationSection = section.getConfigurationSection(LODESTONE_LOCATION);
            if (locationSection == null)
                return;

            Bukkit.getConsoleSender().sendMessage("meow compass");
            Location location = ItemSerializer.getLocation(locationSection, COMPASS);
            if (location != null) {
                Bukkit.getConsoleSender().sendMessage("loading compass rah");
                item.setLodestoneLocation(location);
                item.setLodestoneTracked(true);
            }
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.getLodestoneLocation() == null)
            return;

        Location location = item.getLodestoneLocation();
        section.set(WORLD, location.getWorld().getName());
        section.set(X, location.getX());
        section.set(Y, location.getY());
        section.set(Z, location.getZ());
    }

}
