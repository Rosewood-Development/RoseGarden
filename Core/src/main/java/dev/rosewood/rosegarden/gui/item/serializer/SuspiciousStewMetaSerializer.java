package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * item:
 *   type: suspicious_stew
 *   suspicious-stew:
 *
 */
public class SuspiciousStewMetaSerializer implements MetaSerializer {

    public static final String SUSPICIOUS_STEW = "suspicious-stew";
    public static final String EFFECTS = SUSPICIOUS_STEW + ".effects";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(SUSPICIOUS_STEW) || !section.contains(EFFECTS))
            return;

        if (section.contains(EFFECTS) && section.isConfigurationSection(EFFECTS)) {
            ConfigurationSection effectsSection = section.getConfigurationSection(EFFECTS);
            if (effectsSection == null)
                return;

            for (String id : effectsSection.getKeys(false)) {
                String key = EFFECTS + "." + id + ".";

                if (!effectsSection.contains(key + PotionMetaSerializer.EFFECT_TYPE))
                    continue;

                PotionEffectType type = PotionMetaSerializer.getEffectType(effectsSection.getString(key + PotionMetaSerializer.EFFECT_TYPE));
                if (type == null)
                    continue;

                int amplifier = section.contains(key + PotionMetaSerializer.AMPLIFIER) ? section.getInt(key + PotionMetaSerializer.AMPLIFIER) : 1;
                int duration = section.contains(key + PotionMetaSerializer.DURATION) ? section.getInt(key + PotionMetaSerializer.DURATION) : 20;
                boolean ambient = section.contains(key + PotionMetaSerializer.AMBIENT) && section.getBoolean(key + PotionMetaSerializer.AMBIENT);
                boolean particles = section.contains(key + PotionMetaSerializer.PARTICLES) && section.getBoolean(key + PotionMetaSerializer.PARTICLES);
                boolean icon = section.contains(key + PotionMetaSerializer.ICON) && section.getBoolean(key + PotionMetaSerializer.ICON);

                PotionEffect effect = new PotionEffect(type, amplifier, duration, ambient, particles, icon);
                item.addPotionEffect(effect, true);
            }
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasCustomPotionEffects() || !(item.getMeta() instanceof SuspiciousStewMeta))
            return;

        int index = 0;
        for (PotionEffect effect : item.getCustomPotionEffects()) {
            index++;

            String key = EFFECTS + "." + index + ".";
            section.set(key + PotionMetaSerializer.EFFECT_TYPE, effect.getType().getKey().toString());
            section.set(key + PotionMetaSerializer.AMPLIFIER, effect.getAmplifier());
            section.set(key + PotionMetaSerializer.DURATION, effect.getDuration());

            if (effect.isAmbient())
                section.set(key + PotionMetaSerializer.AMBIENT, true);

            if (effect.hasParticles())
                section.set(key + PotionMetaSerializer.PARTICLES, true);

            if (effect.hasIcon())
                section.set(key + PotionMetaSerializer.ICON, true);
        }
    }

}
