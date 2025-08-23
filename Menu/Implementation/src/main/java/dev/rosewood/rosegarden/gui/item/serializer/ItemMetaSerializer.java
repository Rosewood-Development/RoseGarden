package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.ItemSerializer;
import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

/**
 * item:
 *   type: stick
 *   key: value
 */
@SuppressWarnings("deprecation")
public class ItemMetaSerializer implements MetaSerializer {

    public static final String CUSTOM_MODEL_DATA = "custom-model-data";
    public static final String DISPLAY_NAME = "display-name";
    public static final String LORE = "lore";
    public static final String ENCHANTMENTS = "enchantments";
    public static final String ENCHANTABILITY = "enchantability";
    public static final String DURABILITY = "durability";
    public static final String DAMAGE = "damage";
    public static final String LOCALIZED_NAME = "localized-name";
    public static final String UNBREAKABLE = "unbreakable";
    public static final String ENCHANTMENT_GLINT = "enchantment-glint";
    public static final String FIRE_RESISTANT = "fire-resistant";
    public static final String GLIDER = "is-glider";
    public static final String HIDE_TOOLTIP = "hide-tooltip";
    public static final String ITEM_MODEL = "item-model";
    public static final String ITEM_NAME = "item-name";
    public static final String TOOLTIP_STYLE = "tooltip-style";
    public static final String ITEM_FLAGS = "item-flags";
    public static final String USE_REMAINDER = "use-remainder";
    public static final String RARITY = "rarity";
    public static final String MAX_STACK_SIZE = "max-stack-size";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (section.contains(CUSTOM_MODEL_DATA) && section.isInt(CUSTOM_MODEL_DATA))
            item.setCustomModelData(section.getInt(CUSTOM_MODEL_DATA));

        if (section.contains(DISPLAY_NAME) && section.isString(DISPLAY_NAME))
            item.setDisplayName(section.getString(DISPLAY_NAME));

        if (section.contains(LORE) && section.isList(LORE))
            item.setLore(section.getStringList(LORE));

        if (section.contains(ENCHANTMENTS) && section.isConfigurationSection(ENCHANTMENTS)) {
            ConfigurationSection enchantmentsSection = section.getConfigurationSection(ENCHANTMENTS);
            if (enchantmentsSection != null) {
                for (String enchantmentStr : enchantmentsSection.getKeys(false)) {
                    NamespacedKey key = ItemSerializer.getKey(enchantmentStr, "default");
                    if (key == null)
                        continue;

                    Enchantment enchantment = Enchantment.getByKey(key);
                    int level = enchantmentsSection.getInt(enchantmentStr);

                    item.addEnchantment(enchantment, level);
                }
            }
        }

        if (section.contains(ENCHANTABILITY) && section.isInt(ENCHANTABILITY)) {
            if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
                this.invalidateVersion(ENCHANTABILITY, "1.21.3");
            } else {
                item.setEnchantability(section.getInt(ENCHANTABILITY));
            }
        }

        if (section.contains(DURABILITY) && section.isInt(DURABILITY))
            item.setDurability(section.getInt(DURABILITY));

        if (section.contains(DAMAGE) && section.isInt(DAMAGE)) {
            if (NMSUtil.getVersionNumber() <= 16) {
                this.invalidateVersion(DAMAGE, "1.17");
            } else {
                item.setDamage(section.getInt(DAMAGE));
            }
        }

        if (section.contains(LOCALIZED_NAME) && section.isString(LOCALIZED_NAME))
            item.setLocalizedName(section.getString(LOCALIZED_NAME));

        if (section.contains(UNBREAKABLE) && section.isBoolean(UNBREAKABLE))
            item.setUnbreakable(section.getBoolean(UNBREAKABLE));

        if (section.contains(ENCHANTMENT_GLINT) && section.isBoolean(ENCHANTMENT_GLINT)) {
            if (NMSUtil.getVersionNumber() < 21) {
                this.invalidateVersion(ENCHANTMENT_GLINT, "1.21");
            } else {
                item.setEnchantmentGlint(section.getBoolean(ENCHANTMENT_GLINT));
            }
        }

        if (section.contains(FIRE_RESISTANT) && section.isBoolean(FIRE_RESISTANT)) {
            if (NMSUtil.getVersionNumber() < 21) {
                this.invalidateVersion(FIRE_RESISTANT, "1.21");
            } else {
                item.setFireResistant(section.getBoolean(FIRE_RESISTANT));
            }
        }

        if (section.contains(GLIDER) && section.isBoolean(GLIDER)) {
            if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
                this.invalidateVersion(GLIDER, "1.21.3");
            } else {
                item.setGlider(section.getBoolean(GLIDER));
            }
        }

        if (section.contains(HIDE_TOOLTIP) && section.isBoolean(HIDE_TOOLTIP)) {
            if (NMSUtil.getVersionNumber() < 21) {
                this.invalidateVersion(HIDE_TOOLTIP, "1.21");
            } else {
                item.hideTooltip(section.getBoolean(HIDE_TOOLTIP));
            }
        }

        if (section.contains(ITEM_MODEL) && section.isString(ITEM_MODEL)) {
            if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
                this.invalidateVersion(ITEM_MODEL, "1.21.3");
            } else {
                NamespacedKey key = ItemSerializer.getKey(section.getString(ITEM_MODEL), "default");
                if (key != null)
                    item.setItemModel(key);
            }
        }

        if (section.contains(ITEM_NAME) && section.isString(ITEM_NAME)) {
            if (NMSUtil.getVersionNumber() < 21) {
                this.invalidateVersion(ITEM_NAME, "1.21");
            } else {
                item.setItemName(section.getString(ITEM_NAME));
            }
        }

        if (section.contains(TOOLTIP_STYLE) && section.isString(TOOLTIP_STYLE)) {
            if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
                this.invalidateVersion(TOOLTIP_STYLE, "1.21.3");
            } else {
                NamespacedKey key = ItemSerializer.getKey(section.getString(TOOLTIP_STYLE), "default");
                if (key != null)
                    item.setTooltipStyle(key);
            }
        }

        if (section.contains(ITEM_FLAGS) && section.isList(ITEM_FLAGS)) {
            for (String flagStr : section.getStringList(ITEM_FLAGS)) {
                ItemFlag flag = ItemSerializer.getItemFlag(flagStr);
                item.addItemFlags(flag);
            }
        }

        if (section.contains(USE_REMAINDER) && section.isConfigurationSection(USE_REMAINDER)) {
            if (NMSUtil.getVersionNumber() < 21 || NMSUtil.getMinorVersionNumber() < 3) {
                this.invalidateVersion(USE_REMAINDER, "1.21.3");
            } else {
                ConfigurationSection remainderSection = section.getConfigurationSection(USE_REMAINDER);
                if (remainderSection != null) {
                    RoseItem remainder = ItemSerializer.deserialize(remainderSection);
                    item.setUseRemainder(remainder.asItemStack());
                }
            }
        }

        if (section.contains(RARITY) && section.isString(RARITY)) {
            ItemRarity rarity = getRarity(section.getString(RARITY));
            if (rarity != null)
                item.setRarity(rarity);
        }

        if (section.contains(MAX_STACK_SIZE) && section.isInt(MAX_STACK_SIZE)) {
            if (NMSUtil.getVersionNumber() < 21) {
                this.invalidateVersion(USE_REMAINDER, "1.21");
            } else {
                item.setMaxStackSize(section.getInt(MAX_STACK_SIZE));
            }
        }
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (item.hasCustomModelData())
            section.set(CUSTOM_MODEL_DATA, item.getCustomModelData());

        if (item.hasDisplayName())
            section.set(DISPLAY_NAME, item.getDisplayName());

        if (item.hasLore())
            section.set(LORE, item.getLore());

        if (item.hasEnchantments()) {
            for (Enchantment enchantment : item.getEnchantments().keySet()) {
                String key = enchantment.getKey().toString();
                int level = item.getEnchantmentLevel(enchantment);

                section.set(ENCHANTMENTS + "." + key, level);
            }
        }

        if (item.hasEnchantability())
            section.set(ENCHANTABILITY, item.getEnchantability());

        if (item.getDurability() > 0)
            section.set(DURABILITY, item.getDurability());

        if (item.getDamage() > 0)
            section.set(DAMAGE, item.getDamage());

        if (item.hasLocalizedName())
            section.set(LOCALIZED_NAME, item.getLocalizedName());

        if (item.isUnbreakable())
            section.set(UNBREAKABLE, true);

        if (item.hasEnchantmentGlint())
            section.set(ENCHANTMENT_GLINT, true);

        if (item.isFireResistant())
            section.set(FIRE_RESISTANT, true);

        if (item.isGlider())
            section.set(GLIDER, true);

        if (!item.hasTooltip())
            section.set(HIDE_TOOLTIP, true);

        if (item.hasItemModel())
            section.set(ITEM_MODEL, item.getItemModel().toString());

        if (item.hasItemName())
            section.set(ITEM_NAME, item.getItemName());

        if (item.hasTooltipStyle())
            section.set(TOOLTIP_STYLE, item.getTooltipStyle().toString());

        if (!item.getItemFlags().isEmpty()) {
            List<String> flags = new ArrayList<>();
            for (ItemFlag flag : item.getItemFlags())
                flags.add(flag.toString().toLowerCase());

            section.set(ITEM_FLAGS, flags);
        }

        if (item.hasUseRemainder()) {
            ItemStack remainder = item.getUseRemainder();
            if (remainder != null) {
                RoseItem roseItemRemainder = new RoseItem(remainder);
                ItemSerializer.serialize(roseItemRemainder, section.createSection(USE_REMAINDER));
            }
        }

        if (item.hasRarity())
            section.set(RARITY, item.getRarity().toString().toLowerCase());

        if (item.hasMaxStackSize())
            section.set(MAX_STACK_SIZE, item.getMaxStackSize());
    }

    public static ItemRarity getRarity(String str) {
        try {
            return ItemRarity.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("Invalid item rarity: '" + str + "' for default item meta!");
            return null;
        }
    }

}
