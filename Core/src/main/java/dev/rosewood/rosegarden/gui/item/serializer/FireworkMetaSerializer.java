package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

/**
 * item:
 *   type: firework_rocket
 *   firework:
 *     power: 2
 *     effects:
 *       1:
 *         type: ball
 *         flicker: true
 *         trail: true
 *         fade-colors:
 *           - white
 *         colors:
 *           - red
 *
 */
public class FireworkMetaSerializer implements MetaSerializer {

    public static final String FIREWORK = "firework";
    public static final String EFFECTS = FIREWORK + ".effects";
    public static final String POWER = FIREWORK + ".power";
    public static final String TYPE = "type";
    public static final String FLICKER = "flicker";
    public static final String TRAIL = "trail";
    public static final String FADE_COLORS = "fade-colors";
    public static final String COLORS = "colors";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(FIREWORK))
            return;

        if (section.contains(POWER) && section.isInt(POWER))
            item.setFireworkPower(section.getInt(POWER));

        ConfigurationSection effectsSection = section.getConfigurationSection(EFFECTS);
        if (effectsSection == null)
            return;

        for (String id : effectsSection.getKeys(false)) {
            FireworkEffect.Builder builder = FireworkEffect.builder();
            Bukkit.getConsoleSender().sendMessage("Loading Firework effect: " + id);

            if (effectsSection.contains(id + "." + TYPE) && effectsSection.isString(id + "." + TYPE)) {
                FireworkEffect.Type type = this.getType(effectsSection.getString(id + "." + TYPE));
                if (type != null)
                    builder.with(type);
                Bukkit.getConsoleSender().sendMessage("Type: " + type.toString());
            }

            if (effectsSection.contains(id + "." + FLICKER) && effectsSection.isBoolean(id + "." + TRAIL))
                builder.flicker(effectsSection.getBoolean(id + "." + FLICKER));

            if (effectsSection.contains(id + "." + TRAIL) && effectsSection.isBoolean(id + "." + TRAIL))
                builder.trail(effectsSection.getBoolean(id + "." + TRAIL));

            if (effectsSection.contains(id + "." + FADE_COLORS) && effectsSection.isList(id + "." + FADE_COLORS)) {
                for (String s : effectsSection.getStringList(id + "." + FADE_COLORS)) {
                    Color color = ItemSerializer.getColor(s);
                    if (color != null)
                        builder.withFade(color);
                    Bukkit.getConsoleSender().sendMessage("Fade Color: " + s);
                }
            }

            if (effectsSection.contains(id + "." + COLORS) && effectsSection.isList(id + "." + COLORS)) {
                for (String s : effectsSection.getStringList(id + "." + COLORS)) {
                    Color color = ItemSerializer.getColor(s);
                    if (color != null)
                        builder.withColor(color);
                    Bukkit.getConsoleSender().sendMessage("Color: " + s);
                }
            }

            item.addFireworkEffect(builder.build());
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasFireworkEffects())
            return;

        int index = 0;
        for (FireworkEffect effect : item.getFireworkEffects()) {
            index++;

            String key = EFFECTS + "." + index + ".";
            section.set(key + TYPE, effect.getType().toString().toLowerCase());

            if (effect.hasFlicker())
                section.set(key + FLICKER, true);

            if (effect.hasTrail())
                section.set(key + TRAIL, true);

            List<String> colors = new ArrayList<>();
            for (Color color : effect.getColors()) {
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                String hex = String.format("#%02X%02X%02X", r, g, b);
                colors.add(hex);
            }

            section.set(key + COLORS, colors);

            List<String> fadeColors = new ArrayList<>();
            for (Color color : effect.getFadeColors()) {
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                String hex = String.format("#%02X%02X%02X", r, g, b);
                fadeColors.add(hex);
            }

            section.set(key + FADE_COLORS, fadeColors);
        }

        section.set(POWER, item.getFireworkPower());
    }

    /**
     *     public static final String TYPE = "type";
     *     public static final String FLICKER = "flicker";
     *     public static final String TRAIL = "trail";
     *     public static final String FADE_COLORS = "fade-colors";
     *     public static final String COLORS = "colors";
     * @param str
     * @return
     */

    private FireworkEffect.Type getType(String str) {
        try {
            return FireworkEffect.Type.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
