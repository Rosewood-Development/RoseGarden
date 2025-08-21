package dev.rosewood.rosegarden.gui.item;

import dev.rosewood.rosegarden.gui.item.serializer.ArmorMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.AttributeModifierSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.AxolotlBucketMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.BannerMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.BlockDataMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.BookMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.BundleMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.CompassMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.CrossbowMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.DamageResistantSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.EquippableComponentSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.FireworkMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.FoodComponentSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.ItemMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.JukeboxComponentSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.KnowledgeBookMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.LeatherArmorMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.MapMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.MusicInstrumentMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.OminousBottleMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.PotionMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.RepairableMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.SkullMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.EnchantmentStorageMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.SuspiciousStewMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.ToolComponentSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.TropicalFishBucketMetaSerializer;
import dev.rosewood.rosegarden.gui.item.serializer.UseCooldownComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"deprecation", "removal", "UnstableApiUsage", "unchecked", "unused"})
public final class ItemSerializer {

    private static final Map<String, MetaSerializer> REGISTRY = new HashMap<>();

    public static final ItemMetaSerializer ITEM_META = new ItemMetaSerializer();
    public static final ArmorMetaSerializer ARMOR_META = create(ArmorMetaSerializer.ARMOR, new ArmorMetaSerializer());
    public static final AttributeModifierSerializer ATTRIBUTE_MODIFIER = create(AttributeModifierSerializer.ATTRIBUTES, new AttributeModifierSerializer());
    public static final AxolotlBucketMetaSerializer AXOLOTL_BUCKET_META = create(AxolotlBucketMetaSerializer.AXOLOTL_BUCKET, new AxolotlBucketMetaSerializer());
    public static final BannerMetaSerializer BANNER_META = create(BannerMetaSerializer.BANNER_PATTERNS, new BannerMetaSerializer());
    public static final BlockDataMetaSerializer BLOCK_DATA_META = create(BlockDataMetaSerializer.BLOCK_DATA, new BlockDataMetaSerializer());
    public static final BookMetaSerializer BOOK_META = create(BookMetaSerializer.BOOK, new BookMetaSerializer());
    public static final BundleMetaSerializer BUNDLE_META = create(BundleMetaSerializer.BUNDLE, new BundleMetaSerializer());
    public static final CompassMetaSerializer COMPASS_META = create(CompassMetaSerializer.COMPASS, new CompassMetaSerializer());
    public static final CrossbowMetaSerializer CROSSBOW_META = create(CrossbowMetaSerializer.CROSSBOW, new CrossbowMetaSerializer());
    public static final DamageResistantSerializer DAMAGE_RESISTANT = create(DamageResistantSerializer.DAMAGE_RESISTANT, new DamageResistantSerializer());
    public static final EquippableComponentSerializer EQUIPPABLE_COMPONENT = create(EquippableComponentSerializer.EQUIPPABLE, new EquippableComponentSerializer());
    public static final FireworkMetaSerializer FIREWORK_META = create(FireworkMetaSerializer.FIREWORK, new FireworkMetaSerializer());
    public static final FoodComponentSerializer FOOD_COMPONENT = create(FoodComponentSerializer.FOOD, new FoodComponentSerializer());
    public static final JukeboxComponentSerializer JUKEBOX_COMPONENT = create(JukeboxComponentSerializer.JUKEBOX, new JukeboxComponentSerializer());
    public static final KnowledgeBookMetaSerializer KNOWLEDGE_BOOK_META = create(KnowledgeBookMetaSerializer.KNOWLEDGE_BOOK, new KnowledgeBookMetaSerializer());
    public static final LeatherArmorMetaSerializer LEATHER_ARMOR_META = create(LeatherArmorMetaSerializer.LEATHER_ARMOR, new LeatherArmorMetaSerializer());
    public static final MapMetaSerializer MAP_META = create(MapMetaSerializer.MAP, new MapMetaSerializer());
    public static final MusicInstrumentMetaSerializer MUSIC_INSTRUMENT_META = create(MusicInstrumentMetaSerializer.MUSIC_INSTRUMENT, new MusicInstrumentMetaSerializer());
    public static final OminousBottleMetaSerializer OMINOUS_BOTTLE_META = create(OminousBottleMetaSerializer.OMINOUS_BOTTLE, new OminousBottleMetaSerializer());
    public static final PotionMetaSerializer POTION_META = create(PotionMetaSerializer.POTION, new PotionMetaSerializer());
    public static final RepairableMetaSerializer REPAIRABLE_META = create(RepairableMetaSerializer.REPAIRABLE, new RepairableMetaSerializer());
    public static final SkullMetaSerializer SKULL_META = create(SkullMetaSerializer.SKULL, new SkullMetaSerializer());
    public static final EnchantmentStorageMetaSerializer STORED_ENCHANTMENT_META = create(EnchantmentStorageMetaSerializer.STORED_ENCHANTMENTS, new EnchantmentStorageMetaSerializer());
    public static final SuspiciousStewMetaSerializer SUSPICIOUS_STEW_META = create(SuspiciousStewMetaSerializer.SUSPICIOUS_STEW, new SuspiciousStewMetaSerializer());
    public static final ToolComponentSerializer TOOL_COMPONENT = create(ToolComponentSerializer.TOOL, new ToolComponentSerializer());
    public static final TropicalFishBucketMetaSerializer TROPICAL_FISH_BUCKET_META = create(TropicalFishBucketMetaSerializer.TROPICAL_FISH_BUCKET, new TropicalFishBucketMetaSerializer());
    public static final UseCooldownComponentSerializer USE_COOLDOWN_COMPONENT = create(UseCooldownComponentSerializer.USE_COOLDOWN, new UseCooldownComponentSerializer());

    private ItemSerializer() {

    }

    /**
     * Registers a new {@link MetaSerializer} for serializing item meta.
     * @param id The ID of the serializer.
     * @param serializer An instance of the serializer.
     * @return The serializer.
     */
    public static <T> T create(String id, MetaSerializer serializer) {
        return (T) REGISTRY.put(id, serializer);
    }

    /**
     * @return An unmodifiable map containing the registered {@link MetaSerializer}s.
     */
    public static Map<String, MetaSerializer> getRegistry() {
        return Collections.unmodifiableMap(REGISTRY);
    }

    /**
     * Deserializes a configuration section into a {@link RoseItem}.
     * @param section The {@link ConfigurationSection} to deserialize.
     * @return A deserialized {@link RoseItem}.
     */
    public static RoseItem deserialize(ConfigurationSection section) {
        RoseItem item;
        String typeStr = section.getString("type");
        if (typeStr == null) {
            item = RoseItem.empty();
        } else {
            Material material = getMaterial(typeStr);
            if (material == null)
                item = RoseItem.empty();
            else
                item = RoseItem.of(material);
        }

        if (section.contains("amount") && section.isInt("amount"))
            item.setAmount(section.getInt("amount"));

        ITEM_META.read(item, section);
        for (MetaSerializer serializer : REGISTRY.values())
            serializer.read(item, section);

        return item;
    }

    /**
     * Serializes a {@link RoseItem} into a configuration section.
     * @param item The {@link RoseItem} to serialize.
     * @param section The {@link ConfigurationSection} to hold the serialized item.
     */
    public static void serialize(RoseItem item, ConfigurationSection section) {
        if (!item.isEmpty())
            section.set("type", item.getType().toString().toLowerCase());

        if (item.getAmount() > 1)
            section.set("amount", item.getAmount());

        if (item.getMeta() == null)
            return;

        ITEM_META.write(item, section);
        for (MetaSerializer serializer : REGISTRY.values())
            serializer.write(item, section);
    }

    /**
     * Serializes a {@link RoseItem} into a byte array.
     * @param item The {@link RoseItem} to serialize.
     * @return The byte array containing the item.
     */
    public static byte[] toBytes(RoseItem item) {
        ItemStack itemStack = item.asItemStack();

        byte[] data = new byte[0];
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream(); BukkitObjectOutputStream oos = new BukkitObjectOutputStream(stream)) {
            oos.writeObject(itemStack);
            data = stream.toByteArray();
        } catch (IOException ignored) {
        }

        return data;
    }

    public static RoseItem fromBytes(byte[] data) {
        ItemStack itemStack = null;

        try (ByteArrayInputStream stream = new ByteArrayInputStream(data); BukkitObjectInputStream ois = new BukkitObjectInputStream(stream)) {
            itemStack = (ItemStack) ois.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
        }

        return itemStack == null ? null : new RoseItem(itemStack);
    }

    public static Material getMaterial(String str) {
        try {
            return Material.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid material: '" + str + "'!");
            return null;
        }
    }

    public static NamespacedKey getKey(String keyStr, String meta) {
        if (keyStr == null)
            return null;

        NamespacedKey key = NamespacedKey.fromString(keyStr);
        if (key == null) {
            Bukkit.getLogger().warning("Invalid namespaced key: '" + keyStr + "' for " + meta + " item meta!");
            return null;
        }

        return key;
    }

    public static EquipmentSlot getSlot(String slotStr, String meta) {
        try {
            return EquipmentSlot.valueOf(slotStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid equipment slot: '" + slotStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    public static Sound getSound(String soundStr, String meta) {
        try {
            return Sound.valueOf(soundStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid sound: '" + soundStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    public static EntityType getEntityType(String typeStr, String meta) {
        try {
            return EntityType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid entity type: '" + typeStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    public static ItemFlag getItemFlag(String flagStr) {
        try {
            return ItemFlag.valueOf(flagStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid item flag: '" + flagStr + "'!");
            return null;
        }
    }

    public static DyeColor getDyeColor(String colorStr, String meta) {
        try {
            return DyeColor.valueOf(colorStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid dye color: '" + colorStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    public static BookMeta.Generation getGeneration(String genStr, String meta) {
        try {
            return BookMeta.Generation.valueOf(genStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid book generation: '" + genStr + "' for " + meta + " item meta!");
            return null;
        }
    }

    public static Location getLocation(ConfigurationSection locationSection, String meta) {
        if (locationSection.contains("world") && locationSection.contains("x") &&
                locationSection.contains("y") && locationSection.contains("z")) {
            World world = Bukkit.getWorld(locationSection.getString("world"));
            if (world == null)
                return null;

            double x = locationSection.getDouble("x");
            double y = locationSection.getDouble("y");
            double z = locationSection.getDouble("z");

            return new Location(world, x, y, z);
        }

        return null;
    }

    public static Color getColor(String str) {
        try {
            if (str.startsWith("#") && str.length() == 7) {
                str = str.substring(1);
                int r = Integer.parseInt(str.substring(0, 2), 16);
                int g = Integer.parseInt(str.substring(2, 4), 16);
                int b = Integer.parseInt(str.substring(4, 6), 16);
                return Color.WHITE;
            } else {
                Bukkit.getLogger().warning("");
                return null;
            }
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("");
            return null;
        }
    }

}
