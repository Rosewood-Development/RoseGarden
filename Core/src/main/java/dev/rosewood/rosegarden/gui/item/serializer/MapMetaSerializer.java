package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

/**
 * item:
 *   type: map
 *   map:
 *     id: 2
 *     color: #112233
 *     scale: closest
 *     centre-x: 120
 *     centre-y: 220
 *     world: world
 *     tracking: true
 *     unlimited-tracking: true
 *     locked: false
 */
public class MapMetaSerializer implements MetaSerializer {

    public static final String MAP = "map";
    public static final String ID = MAP + ".id";
    public static final String COLOR = MAP + ".color";
    public static final String SCALE = MAP + ".scale";
    public static final String CENTRE_X = MAP + ".centre-x";
    public static final String CENTRE_Z = MAP + ".centre-Z";
    public static final String WORLD = MAP + ".world";
    public static final String TRACKING = MAP + ".tracking";
    public static final String UNLIMITED_TRACKING = MAP + ".unlimited-tracking";
    public static final String LOCKED = MAP + ".locked";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(MAP))
            return;

        if (section.contains(ID) && section.isInt(ID))
            item.setMapId(section.getInt(ID));

        if (section.contains(COLOR) && section.isString(COLOR)) {
            Color color = ItemSerializer.getColor(section.getString(COLOR));
            if (color != null)
                item.setColor(color);
        }

        if (section.contains(SCALE) && section.isString(SCALE)) {
            MapView.Scale scale = this.getScale(section.getString(SCALE));
            if (scale != null) {
                item.setMapScale(scale);
            }
        }

        if (section.contains(CENTRE_X) && section.isInt(CENTRE_X))
            item.setMapCentreX(section.getInt(CENTRE_X));

        if (section.contains(CENTRE_Z) && section.isInt(CENTRE_Z))
            item.setMapCentreZ(section.getInt(CENTRE_Z));

        if (section.contains(WORLD) && section.isString(WORLD)) {
            World world = Bukkit.getWorld(section.getString(WORLD));
            if (world != null)
                item.setMapWorld(world);
        }

        if (section.contains(TRACKING) && section.isBoolean(TRACKING))
            item.setMapTracking(section.getBoolean(TRACKING));

        if (section.contains(UNLIMITED_TRACKING) && section.isBoolean(UNLIMITED_TRACKING))
            item.setMapTrackingUnlimited(section.getBoolean(UNLIMITED_TRACKING));

        if (section.contains(LOCKED) && section.isBoolean(LOCKED))
            item.setMapLocked(section.getBoolean(LOCKED));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!(item.getMeta() instanceof MapMeta))
            return;

        if (item.getMapId() != -1)
            section.set(ID, item.getMapId());

        if (item.hasColor()) {
            Color color = item.getColor();
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            String hex = String.format("#%02X%02X%02X", r, g, b);
            section.set(COLOR, hex);
        }

        if (item.getMapScale() != null)
            section.set(SCALE, item.getMapScale().toString().toLowerCase());

        if (item.getMapCentreX() != null)
            section.set(CENTRE_X, item.getMapCentreX());

        if (item.getMapCentreZ() != null)
            section.set(CENTRE_Z, item.getMapCentreZ());

        if (item.getMapWorld() != null)
            section.set(WORLD, item.getMapWorld().getName());

        if (item.isMapTracking())
            section.set(TRACKING, true);

        if (item.isMapTrackingUnlimited())
            section.set(UNLIMITED_TRACKING, true);

        if (item.isMapLocked())
            section.set(LOCKED, true);
    }

    public MapView.Scale getScale(String str) {
        try {
            return MapView.Scale.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("");
            return null;
        }
    }

}
