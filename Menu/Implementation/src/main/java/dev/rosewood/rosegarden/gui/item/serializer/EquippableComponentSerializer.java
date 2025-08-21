package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.components.EquippableComponent;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class EquippableComponentSerializer implements MetaSerializer {

    public static final String EQUIPPABLE = "equippable";
    public static final String SLOT = EQUIPPABLE + ".slot";
    public static final String SOUND = EQUIPPABLE + ".sound";
    public static final String MODEL = EQUIPPABLE + ".model";
    public static final String OVERLAY = EQUIPPABLE + ".overlay";
    public static final String ENTITIES = EQUIPPABLE + ".entities";
    public static final String DISPENSABLE = EQUIPPABLE + ".dispensable";
    public static final String SWAPPABLE = EQUIPPABLE + ".swappable";
    public static final String DAMAGE_ON_HURT = EQUIPPABLE + ".damage-on-hurt";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(EQUIPPABLE))
            return;

        if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
            Bukkit.getLogger().warning("The " + EQUIPPABLE + " item meta is only available on 1.21.3+!");
            return;
        }

        EquippableComponent equippableComponent = item.getEquippableComponent();

        if (section.contains(SLOT)) {
            EquipmentSlot slot = ItemSerializer.getSlot(section.getString(SLOT), EQUIPPABLE);
            if (slot != null)
                equippableComponent.setSlot(slot);
        }

        if (section.contains(SOUND)) {
            Sound sound = ItemSerializer.getSound(section.getString(SOUND), EQUIPPABLE);
            if (sound != null)
                equippableComponent.setEquipSound(sound);
        }

        if (section.contains(MODEL)) {
            NamespacedKey key = ItemSerializer.getKey(section.getString(MODEL), EQUIPPABLE);
            if (key != null)
                equippableComponent.setModel(key);
        }

        if (section.contains(OVERLAY)) {
            NamespacedKey key = ItemSerializer.getKey(section.getString(OVERLAY), EQUIPPABLE);
            if (key != null)
                equippableComponent.setCameraOverlay(key);
        }

        if (section.contains(ENTITIES)) {
            List<EntityType> entities = new ArrayList<>();

            for (String str : section.getStringList(ENTITIES)) {
                EntityType type = ItemSerializer.getEntityType(str, EQUIPPABLE);
                if (type != null)
                    entities.add(type);
            }

            equippableComponent.setAllowedEntities(entities);
        }

        if (section.contains(DISPENSABLE))
            equippableComponent.setDispensable(section.getBoolean(DISPENSABLE));

        if (section.contains(SWAPPABLE))
            equippableComponent.setSwappable(section.getBoolean(SWAPPABLE));

        if (section.contains(DAMAGE_ON_HURT))
            equippableComponent.setDamageOnHurt(section.getBoolean(DAMAGE_ON_HURT));

        item.setEquippableComponent(equippableComponent);
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasEquippableComponent())
            return;

        EquippableComponent equippableComponent = item.getEquippableComponent();

        if (equippableComponent.getSlot() != null)
            section.set(SLOT, equippableComponent.getSlot().toString().toLowerCase());

        if (equippableComponent.getEquipSound() != null)
            section.set(SOUND, equippableComponent.getEquipSound().toString().toLowerCase());

        if (equippableComponent.getModel() != null)
            section.set(MODEL, equippableComponent.getModel().toString());

        if (equippableComponent.getCameraOverlay() != null)
            section.set(OVERLAY, equippableComponent.getCameraOverlay().toString());

        if (!equippableComponent.getAllowedEntities().isEmpty()) {
            List<String> entities = new ArrayList<>();
            for (EntityType type : equippableComponent.getAllowedEntities())
                entities.add(type.toString().toLowerCase());

            section.set(ENTITIES, entities);
        }

        if (equippableComponent.isDispensable())
            section.set(DISPENSABLE, true);

        if (equippableComponent.isSwappable())
            section.set(SWAPPABLE, true);

        if (equippableComponent.isDamageOnHurt())
            section.set(DAMAGE_ON_HURT, true);
    }

}
