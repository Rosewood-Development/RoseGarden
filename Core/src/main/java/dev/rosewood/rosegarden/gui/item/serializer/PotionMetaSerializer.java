package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 * item:
 *   type: potion
 *   potion:
 *     data: 10
 *     type: speed
 *     color: #0000FF
 *     effects:
 *       1:
 *         type: strength
 *         amplifier: 10
 *         duration: 100
 *         ambient: false
 *         particles: true
 *         icon: false
 */

@SuppressWarnings({"deprecated", "removal"})
public class PotionMetaSerializer implements MetaSerializer {

    public static final String POTION = "potion";
    public static final String DATA = POTION + ".data";
    public static final String DATA_TYPE = DATA + ".type";
    public static final String DATA_EXTENDED = DATA + ".extended";
    public static final String DATA_UPGRADED = DATA + ".upgraded";
    public static final String TYPE = POTION + ".type";
    public static final String EFFECTS = POTION + ".effects";
    public static final String COLOR = POTION + ".color";
    public static final String EFFECT_TYPE = "type";
    public static final String AMPLIFIER = "amplifier";
    public static final String DURATION = "duration";
    public static final String AMBIENT = "ambient";
    public static final String PARTICLES = "particles";
    public static final String ICON = "icon";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(POTION))
            return;

        if (section.contains(DATA) && section.isConfigurationSection(DATA)) {
            if (section.contains(DATA_TYPE)) {
                PotionType type = this.getPotionType(section.getString(DATA_TYPE));
                if (type != null) {
                    boolean extended = section.contains(DATA_EXTENDED) && section.getBoolean(DATA_EXTENDED);
                    boolean upgraded = section.contains(DATA_UPGRADED) && section.getBoolean(DATA_UPGRADED);
                    PotionData data = new PotionData(type, extended, upgraded);
                    item.setPotionData(data);
                }
            }
        }

        if (section.contains(TYPE) && section.isString(TYPE)) {
            PotionType type = this.getPotionType(section.getString(TYPE));
            if (type != null)
                item.setPotionType(type);
        }

        if (section.contains(COLOR) && section.isString(COLOR)) {
            Color color = ItemSerializer.getColor(section.getString(COLOR));
            if (color != null)
                item.setColor(color);
        }

        if (section.contains(EFFECTS) && section.isConfigurationSection(EFFECTS)) {
            ConfigurationSection effectsSection = section.getConfigurationSection(EFFECTS);
            if (effectsSection == null)
                return;

            for (String id : effectsSection.getKeys(false)) {
                String key = EFFECTS + "." + id + ".";

                if (!effectsSection.contains(key + EFFECT_TYPE))
                    continue;

                PotionEffectType type = getEffectType(effectsSection.getString(key + EFFECT_TYPE));
                if (type == null)
                    continue;

                int amplifier = section.contains(key + AMPLIFIER) ? section.getInt(key + AMPLIFIER) : 1;
                int duration = section.contains(key + DURATION) ? section.getInt(key + DURATION) : 20;
                boolean ambient = section.contains(key + AMBIENT) && section.getBoolean(key + AMBIENT);
                boolean particles = section.contains(key + PARTICLES) && section.getBoolean(key + PARTICLES);
                boolean icon = section.contains(key + ICON) && section.getBoolean(key + ICON);

                PotionEffect effect = new PotionEffect(type, amplifier, duration, ambient, particles, icon);
                item.addPotionEffect(effect, true);
            }
        }

    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!(item.getMeta() instanceof PotionMeta))
            return;

        if (item.getBasePotionData() != null) {
            PotionData data = item.getBasePotionData();
            section.set(DATA_TYPE, data.getType().toString().toLowerCase());

            if (data.isUpgraded())
                section.set(DATA_UPGRADED, true);

            if (data.isExtended())
                section.set(DATA_EXTENDED, true);
        }

        if (item.getColor() != null) {
            Color color = item.getColor();
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            String hex = String.format("#%02X%02X%02X", r, g, b);

            section.set(COLOR, hex);
        }

        if (!item.hasCustomPotionEffects())
            return;

        int index = 0;
        for (PotionEffect effect : item.getCustomPotionEffects()) {
            index++;

            String key = EFFECTS + "." + index + ".";
            section.set(key + EFFECT_TYPE, effect.getType().getKey().toString());
            section.set(key + AMPLIFIER, effect.getAmplifier());
            section.set(key + DURATION, effect.getDuration());

            if (effect.isAmbient())
                section.set(key + AMBIENT, true);

            if (effect.hasParticles())
                section.set(key + PARTICLES, true);

            if (effect.hasIcon())
                section.set(key + ICON, true);
        }
    }

    private PotionType getPotionType(String str) {
        try {
            return PotionType.valueOf(str);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("");
            return null;
        }
    }

    public static PotionEffectType getEffectType(String str) {
        return PotionEffectType.getByName(str);
    }

}
