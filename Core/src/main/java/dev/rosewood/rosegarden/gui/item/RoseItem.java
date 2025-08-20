package dev.rosewood.rosegarden.gui.item;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.rosewood.rosegarden.gui.parameter.Context;
import dev.rosewood.rosegarden.hook.PlaceholderAPIHook;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.OminousBottleMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A wrapper for easier handling of item stacks and item meta.
 */
@SuppressWarnings({"deprecation", "removal", "unused", "unchecked", "UnstableApiUsage", "UnusedReturnValue"})
public class RoseItem implements Item {

    private Method setSkullMetaProfileMethod;

    private final ItemStack stack;
    private final ItemMeta meta;
    private boolean isEmpty;
    private final StringPlaceholders.Builder placeholders;

    // Constructors

    public RoseItem(Material material) {
        this.stack = new ItemStack(material);
        this.meta = this.stack.getItemMeta();
        this.placeholders = StringPlaceholders.builder();
    }

    public RoseItem(ItemStack stack) {
        this.stack = stack.clone();
        this.meta = stack.getItemMeta().clone();
        this.placeholders = StringPlaceholders.builder();
    }

    public RoseItem(ItemStack stack, ItemMeta meta) {
        this.stack = stack;
        this.meta = meta;
        this.placeholders = StringPlaceholders.builder();
    }

    // RoseItem Methods

    public RoseItem addPlaceholders(StringPlaceholders placeholders) {
        this.placeholders.addAll(placeholders);
        return this;
    }

    public RoseItem addPlaceholder(String placeholder, Object value) {
        this.placeholders.add(placeholder, value);
        return this;
    }

    public RoseItem setEmpty(boolean empty) {
        this.isEmpty = empty;
        return this;
    }

    public RoseItem setEmpty() {
        return this.setEmpty(true);
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    public ItemStack asItemStack() {
        this.stack.setItemMeta(this.meta);
        return this.stack;
    }

    private String applyPlaceholders(OfflinePlayer player, StringPlaceholders placeholders, String str) {
        return HexUtils.colorify(PlaceholderAPIHook.applyPlaceholders(player, placeholders.apply(str)));
    }

    public ItemStack applyPlaceholders(OfflinePlayer player) {
        StringPlaceholders placeholders = this.placeholders.build();

        if (this.hasDisplayName())
            this.setDisplayName(this.applyPlaceholders(player, placeholders, this.getDisplayName()));

        if (this.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String s : this.getLore())
                lore.add(this.applyPlaceholders(player, placeholders, s));

            this.setLore(lore);
        }

        if (this.hasItemName())
            this.setItemName(this.applyPlaceholders(player, placeholders, this.getItemName()));

        if (this.hasLocalizedName())
            this.setLocalizedName(this.applyPlaceholders(player, placeholders, this.getLocalizedName()));

        if (this.hasSkullOwner())
            this.setSkullOwner(PlaceholderAPIHook.applyPlaceholders(player, placeholders.apply(this.getSkullOwner())));

        if (this.hasBookTitle())
            this.setBookTitle(this.applyPlaceholders(player, placeholders, this.getBookTitle()));

        if (this.hasBookAuthor())
            this.setBookAuthor(this.applyPlaceholders(player, placeholders, this.getBookAuthor()));

        if (this.hasBookPages()) {
            List<String> pages = new ArrayList<>();
            for (String s : this.getBookPages())
                pages.add(this.applyPlaceholders(player, placeholders, s));

            this.setBookPages(pages);
        }

        return this.asItemStack();
    }

    // Stack Methods

    public RoseItem setType(Material type) {
        this.stack.setType(type);
        return this;
    }

    public Material getType() {
        return this.stack.getType();
    }

    public RoseItem setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return this.stack.getAmount();
    }

    // Meta Methods

    public ItemMeta getMeta() {
        return this.meta;
    }

    // Custom Model Data

    public RoseItem setCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    public int getCustomModelData() {
        return this.meta.getCustomModelData();
    }

    public boolean hasCustomModelData() {
        return this.meta.hasCustomModelData();
    }

    // Display Name

    public RoseItem setDisplayName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    public String getDisplayName() {
        return this.meta.getDisplayName();
    }

    public boolean hasDisplayName() {
        return this.meta.hasDisplayName();
    }

    // Lore

    public RoseItem addLore(String... lore) {
        List<String> newLore = this.getLore() == null ?
                new ArrayList<>() : this.getLore();
        newLore.addAll(Arrays.asList(lore));
        return this.setLore(newLore);
    }

    public RoseItem setLore(String... lore) {
        this.meta.setLore(Arrays.asList(lore));
        return this;
    }

    public RoseItem setLore(List<String> lore) {
        this.meta.setLore(lore);
        return this;
    }

    public List<String> getLore() {
        return this.meta.getLore();
    }

    public boolean hasLore() {
        return this.meta.hasLore();
    }

    // Enchantments

    public RoseItem addEnchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public RoseItem setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.meta.removeEnchantments();
        return this.addEnchantments(enchantments);
    }

    public RoseItem removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public RoseItem addEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : enchantments.keySet())
            this.addEnchantment(enchantment, enchantments.get(enchantment));

        return this;
    }

    public int getEnchantmentLevel(Enchantment enchantment) {
        return this.meta.getEnchantLevel(enchantment);
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.meta.getEnchants();
    }

    public boolean hasConflictingEnchantment(Enchantment enchantment) {
        return this.meta.hasConflictingEnchant(enchantment);
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        return this.meta.hasEnchant(enchantment);
    }

    public boolean hasEnchantments() {
        return this.meta.hasEnchants();
    }

    // Enchantability

    public RoseItem setEnchantability(Integer enchantability) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setEnchantable(enchantability);

        return this;
    }

    public int getEnchantability() {
        return this.meta.getEnchantable();
    }

    public boolean hasEnchantability() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasEnchantable();
    }

    // Durability

    public RoseItem setDurability(int durability) {
        if (NMSUtil.getVersionNumber() <= 16)
            this.stack.setDurability((short) durability);

        return this;
    }

    public int getDurability() {
        if (NMSUtil.getVersionNumber() <= 16)
            return this.stack.getDurability();

        return -1;
    }

    // Damage

    public RoseItem setDamage(int damage) {
        if (NMSUtil.getVersionNumber() > 16) {
            if (this.meta instanceof Damageable)
                ((Damageable) this.meta).setDamage(damage);
        }

        return this;
    }

    public int getDamage() {
        if (NMSUtil.getVersionNumber() > 16) {
            if (this.meta instanceof Damageable)
                return ((Damageable) this.meta).getDamage();
        }

        return -1;
    }

    // Localized Name

    public RoseItem setLocalizedName(String name) {
        this.meta.setLocalizedName(name);
        return this;
    }

    public String getLocalizedName() {
        return this.meta.getLocalizedName();
    }

    public boolean hasLocalizedName() {
        return this.meta.hasLocalizedName();
    }

    // Unbreakable

    public RoseItem setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public RoseItem setUnbreakable() {
        return this.setUnbreakable(true);
    }

    public boolean isUnbreakable() {
        return this.meta.isUnbreakable();
    }

    // Enchantment Glint

    public RoseItem setEnchantmentGlint(boolean override) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setEnchantmentGlintOverride(override);

        return this;
    }

    public RoseItem setEnchantmentGlint() {
        return this.setEnchantmentGlint(true);
    }

    public boolean getEnchantmentGlint() {
        return NMSUtil.getVersionNumber() >= 21 ? this.meta.getEnchantmentGlintOverride() : false;
    }

    public boolean hasEnchantmentGlint() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasEnchantmentGlintOverride();
    }

    // Fire Resistant

    public RoseItem setFireResistant(boolean fireResistant) {
        if (NMSUtil.getVersionNumber() >= 21 && fireResistant)
            this.meta.setFireResistant(true);

        return this;
    }

    public RoseItem setFireResistant() {
        return this.setFireResistant(true);
    }

    public boolean isFireResistant() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.isFireResistant();
    }

    // Glider

    public RoseItem setGlider(boolean glider) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setGlider(glider);

        return this;
    }

    public RoseItem setGlider() {
        return this.setGlider(true);
    }

    public boolean isGlider() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.isGlider();
    }

    // Hide Tooltip

    public RoseItem hideTooltip(boolean hideTooltip) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setHideTooltip(hideTooltip);

        return this;
    }

    public RoseItem hideTooltip() {
        return this.hideTooltip(true);
    }

    public boolean hasTooltip() {
        return NMSUtil.getVersionNumber() >= 21 && !this.meta.isHideTooltip();
    }

    // Item Model

    public RoseItem setItemModel(NamespacedKey itemModel) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setItemModel(itemModel);

        return this;
    }

    public NamespacedKey getItemModel() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 ?
                this.meta.getItemModel() : null;
    }

    public boolean hasItemModel() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasItemModel();
    }

    // Item Name

    public RoseItem setItemName(String name) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setItemName(name);

        return this;
    }

    public String getItemName() {
        return NMSUtil.getVersionNumber() >= 21 ? this.meta.getItemName() : null;
    }

    public boolean hasItemName() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasItemName();
    }

    // Tooltip Style

    public RoseItem setTooltipStyle(NamespacedKey tooltipStyle) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setTooltipStyle(tooltipStyle);

        return this;
    }

    public NamespacedKey getTooltipStyle() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 ?
                this.meta.getTooltipStyle() : null;
    }

    public boolean hasTooltipStyle() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasTooltipStyle();
    }

    // Item Flags

    public RoseItem addItemFlags(ItemFlag... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        return this;
    }

    public RoseItem addItemFlags(Set<ItemFlag> itemFlags) {
        for (ItemFlag flag : itemFlags)
            this.addItemFlags(flag);

        return this;
    }

    public RoseItem removeItemFlags(ItemFlag... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        return this;
    }

    public Set<ItemFlag> getItemFlags() {
        return this.meta.getItemFlags();
    }

    public boolean hasItemFlag(ItemFlag flag) {
        return this.meta.hasItemFlag(flag);
    }

    // Use Remainder

    public RoseItem setUseRemainder(ItemStack remainder) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setUseRemainder(remainder);

        return this;
    }

    public ItemStack getUseRemainder() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3?
                this.meta.getUseRemainder() : null;
    }

    public boolean hasUseRemainder() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasUseRemainder();
    }

    // Armor Meta

    public <T> RoseItem setArmorTrim(T trim) {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof ArmorMeta)
            ((ArmorMeta) this.meta).setTrim((ArmorTrim) trim);

        return this;
    }

    public <T> T getArmorTrim() {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof ArmorMeta)
            return (T) ((ArmorMeta) this.meta).getTrim();

        return null;
    }

    public boolean hasArmorTrim() {
        return NMSUtil.getVersionNumber() >= 19 && this.meta instanceof ArmorMeta && ((ArmorMeta) this.meta).hasTrim();
    }

    // Attributes

    public RoseItem addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        this.meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public RoseItem addAttributeModifier(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        return this.addAttributeModifier(attribute, new AttributeModifier(attribute.getKey().toString(), amount, operation));
    }

    public RoseItem addAttributeModifiers(Multimap<Attribute, AttributeModifier> attributes) {
        for (Attribute attribute : attributes.keys()) {
            Collection<AttributeModifier> modifiers = attributes.get(attribute);
            for (AttributeModifier modifier : modifiers)
                this.addAttributeModifier(attribute, modifier);
        }

        return this;
    }

    public RoseItem setAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        this.meta.setAttributeModifiers(attributeModifiers);
        return this;
    }

    public RoseItem removeAttributeModifier(Attribute attribute) {
        this.meta.removeAttributeModifier(attribute);
        return this;
    }

    public RoseItem removeAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        this.meta.removeAttributeModifier(attribute, modifier);
        return this;
    }

    public RoseItem removeAttributeModifier(EquipmentSlot slot) {
        this.meta.removeAttributeModifier(slot);
        return this;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.meta.getAttributeModifiers();
    }

    public Collection<AttributeModifier> getAttributeModifiers(Attribute attribute) {
        return this.meta.getAttributeModifiers(attribute);
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return this.meta.getAttributeModifiers(slot);
    }

    public boolean hasAttributeModifiers() {
        return this.meta.hasAttributeModifiers();
    }

    // Axolotl Bucket Meta

    public <T> RoseItem setAxolotlVariant(T variant) {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof AxolotlBucketMeta)
            ((AxolotlBucketMeta) this.meta).setVariant((Axolotl.Variant) variant);

        return this;
    }

    public <T> T getAxolotlVariant() {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof AxolotlBucketMeta) {
            return (T) ((AxolotlBucketMeta) this.meta).getVariant();
        }

        return null;
    }

    public boolean hasAxolotlVariant() {
        if (NMSUtil.getVersionNumber() >= 17)
            return this.meta instanceof AxolotlBucketMeta && ((AxolotlBucketMeta) this.meta).hasVariant();
        else
            return false;
    }

    // Banner Meta

    public RoseItem addBannerPattern(Pattern pattern) {
        if (this.meta instanceof BannerMeta)
            ((BannerMeta) this.meta).addPattern(pattern);

        return this;
    }

    public RoseItem addBannerPatterns(List<Pattern> patterns) {
        if (this.meta instanceof BannerMeta)
            for (Pattern pattern : patterns)
                this.addBannerPattern(pattern);

        return this;
    }

    public RoseItem removeBannerPattern(int i) {
        if (this.meta instanceof BannerMeta)
            ((BannerMeta) this.meta).removePattern(i);

        return this;
    }

    public RoseItem setBannerPattern(int i, Pattern pattern) {
        if (this.meta instanceof BannerMeta)
            ((BannerMeta) this.meta).setPattern(i, pattern);

        return this;
    }

    public RoseItem setBannerPatterns(List<Pattern> patterns) {
        if (this.meta instanceof BannerMeta)
            ((BannerMeta) this.meta).setPatterns(patterns);

        return this;
    }

    public Pattern getBannerPattern(int i) {
        if (this.meta instanceof BannerMeta)
            return ((BannerMeta) this.meta).getPattern(i);

        return null;
    }

    public List<Pattern> getBannerPatterns() {
        if (this.meta instanceof BannerMeta)
            return ((BannerMeta) this.meta).getPatterns();

        return null;
    }

    public int getNumberOfBannerPatterns() {
        if (this.meta instanceof BannerMeta)
            return ((BannerMeta) this.meta).numberOfPatterns();

        return 0;
    }

    // Block Data Meta

    public RoseItem setBlockData(BlockData blockData) {
        if (this.meta instanceof BlockDataMeta)
            ((BlockDataMeta) this.meta).setBlockData(blockData);

        return this;
    }

    public BlockData getBlockData(Material material) {
        if (this.meta instanceof BlockDataMeta)
            return ((BlockDataMeta) this.meta).getBlockData(material);

        return null;
    }

    public boolean hasBlockData() {
        if (this.meta instanceof BlockDataMeta)
            return ((BlockDataMeta) this.meta).hasBlockData();

        return false;
    }

    // Block State Meta

    public RoseItem setBlockState(BlockState blockState) {
        if (this.meta instanceof BlockStateMeta)
            ((BlockStateMeta) this.meta).setBlockState(blockState);

        return this;
    }

    public BlockState getBlockState() {
        if (this.meta instanceof BlockStateMeta)
            return ((BlockStateMeta) this.meta).getBlockState();

        return null;
    }

    public boolean hasBlockState() {
        if (this.meta instanceof BlockStateMeta)
            return ((BlockStateMeta) this.meta).hasBlockState();

        return false;
    }

    // Book Meta

    public RoseItem addBookPage(String... pages) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).addPage(pages);
        else if (this.meta instanceof WritableBookMeta)
            ((WritableBookMeta) this.meta).addPage(pages);

        return this;
    }

    public RoseItem setBookAuthor(String author) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setAuthor(author);

        return this;
    }

    public RoseItem setBookGeneration(BookMeta.Generation generation) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setGeneration(generation);

        return this;
    }

    public RoseItem setBookPage(int page, String data) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setPage(page, data);
        else if (this.meta instanceof WritableBookMeta)
            ((WritableBookMeta) this.meta).setPage(page, data);

        return this;
    }

    public RoseItem setBookPages(String... pages) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setPages(pages);
        else if (this.meta instanceof WritableBookMeta)
            ((WritableBookMeta) this.meta).setPages(pages);

        return this;
    }

    public RoseItem setBookPages(List<String> pages) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setPages(pages);
        else if (this.meta instanceof WritableBookMeta)
            ((WritableBookMeta) this.meta).setPages(pages);

        return this;
    }

    public RoseItem setBookTitle(String title) {
        if (this.meta instanceof BookMeta)
            ((BookMeta) this.meta).setTitle(title);

        return this;
    }

    public String getBookAuthor() {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getAuthor();

        return null;
    }

    public BookMeta.Generation getBookGeneration() {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getGeneration();

        return null;
    }

    public String getBookPage(int page) {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getPage(page);
        else if (this.meta instanceof WritableBookMeta)
            return ((WritableBookMeta) this.meta).getPage(page);

        return null;
    }

    public int getBookPageCount() {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getPageCount();
        else if (this.meta instanceof WritableBookMeta)
            return ((WritableBookMeta) this.meta).getPageCount();

        return 0;
    }

    public List<String> getBookPages() {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getPages();
        else if (this.meta instanceof WritableBookMeta)
            return ((WritableBookMeta) this.meta).getPages();

        return null;
    }

    public String getBookTitle() {
        if (this.meta instanceof BookMeta)
            return ((BookMeta) this.meta).getTitle();

        return null;
    }

    public boolean hasBookAuthor() {
        return this.meta instanceof BookMeta && ((BookMeta) this.meta).hasAuthor();
    }

    public boolean hasBookGeneration() {
        return this.meta instanceof BookMeta && ((BookMeta) this.meta).hasGeneration();
    }

    public boolean hasBookPages() {
        return (this.meta instanceof BookMeta && ((BookMeta) this.meta).hasPages())
                || (this.meta instanceof WritableBookMeta && ((WritableBookMeta) this.meta).hasPages());
    }

    public boolean hasBookTitle() {
        return this.meta instanceof BookMeta && ((BookMeta) this.meta).hasTitle();
    }

    // Bundle Meta

    public RoseItem addBundleItem(ItemStack item) {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof BundleMeta)
            ((BundleMeta) this.meta).addItem(item);

        return this;
    }

    public RoseItem addBundleItems(List<ItemStack> items) {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof BundleMeta)
            for (ItemStack item : items)
                this.addBundleItem(item);

        return this;
    }

    public RoseItem setBundleItems(List<ItemStack> items) {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof BundleMeta)
            ((BundleMeta) this.meta).setItems(items);

        return this;
    }

    public List<ItemStack> getBundleItems() {
        if (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof BundleMeta)
            return ((BundleMeta) this.meta).getItems();

        return null;
    }

    public boolean hasBundleItems() {
        return (NMSUtil.getVersionNumber() >= 17 && this.meta instanceof BundleMeta && ((BundleMeta) this.meta).hasItems());
    }

    // Compass Meta

    public RoseItem setLodestoneLocation(Location location) {
        if (this.meta instanceof CompassMeta)
            ((CompassMeta) this.meta).setLodestone(location);

        return this;
    }

    public RoseItem setLodestoneTracked(boolean tracked) {
        if (this.meta instanceof CompassMeta)
            ((CompassMeta) this.meta).setLodestoneTracked(tracked);

        return this;
    }

    public Location getLodestoneLocation() {
        return this.meta instanceof CompassMeta ? ((CompassMeta) this.meta).getLodestone() : null;
    }

    public boolean hasLodestone() {
        return this.meta instanceof CompassMeta && ((CompassMeta) this.meta).hasLodestone();
    }

    public boolean isLodestoneTracked() {
        return this.meta instanceof CompassMeta && ((CompassMeta) this.meta).isLodestoneTracked();
    }

    // Crossbow Meta

    public RoseItem addCrossbowProjectile(ItemStack stack) {
        if (this.meta instanceof CrossbowMeta)
            ((CrossbowMeta) this.meta).addChargedProjectile(stack);

        return this;
    }

    public RoseItem setCrossbowProjectiles(List<ItemStack> projectiles) {
        if (this.meta instanceof CrossbowMeta)
            ((CrossbowMeta) this.meta).setChargedProjectiles(projectiles);

        return this;
    }

    public List<ItemStack> getCrossbowProjectiles() {
        if (this.meta instanceof CrossbowMeta)
            return ((CrossbowMeta) this.meta).getChargedProjectiles();

        return null;
    }

    public boolean hasCrossbowProjectiles() {
        if (this.meta instanceof CrossbowMeta)
            return ((CrossbowMeta) this.meta).hasChargedProjectiles();

        return false;
    }

    // Damage Resistant

    public RoseItem setDamageResistant(Tag<DamageType> tag) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setDamageResistant(tag);

        return this;
    }

    public Tag<DamageType> getDamageResistant() {
        return this.meta.getDamageResistant();
    }

    public boolean hasDamageResistant() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasDamageResistant();
    }

    // Equippable Component

    public <T> RoseItem setEquippableComponent(T equippableComponent) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setEquippable((EquippableComponent) equippableComponent);

        return this;
    }

    public <T> T getEquippableComponent() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 ?
                (T) this.meta.getEquippable() : null;
    }

    public boolean hasEquippableComponent() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasEquippable();
    }

    public RoseItem setEquippableSlot(EquipmentSlot slot) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setSlot(slot);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public EquipmentSlot getEquippableSlot() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.getSlot();
        }

        return null;
    }

    public RoseItem setEquipSound(Sound sound) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setEquipSound(sound);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public Sound getEquipSound() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.getEquipSound();
        }

        return null;
    }

    public RoseItem setEquippableModel(NamespacedKey key) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setModel(key);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public NamespacedKey getEquippableModel() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.getModel();
        }

        return null;
    }

    public RoseItem setEquippableOverlay(NamespacedKey key) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setCameraOverlay(key);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public NamespacedKey getEquippableOverlay() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.getCameraOverlay();
        }

        return null;
    }

    public RoseItem setEquippableEntities(List<EntityType> entityTypes) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setAllowedEntities(entityTypes);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public RoseItem setEquippableEntities(EntityType... entityTypes) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setAllowedEntities(Arrays.asList(entityTypes));
            this.setEquippableComponent(component);
        }

        return this;
    }

    public Collection<EntityType> getEquippableEntities() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.getAllowedEntities();
        }

        return null;
    }

    public RoseItem setEquippableDispensable(boolean dispensable) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setDispensable(dispensable);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public RoseItem setEquippableDispensable() {
        return this.setEquippableDispensable(true);
    }

    public boolean isEquippableDispensable() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.isDispensable();
        }

        return false;
    }

    public RoseItem setEquippableSwappable(boolean swappable) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setSwappable(swappable);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public RoseItem setEquippableSwappable() {
        return this.setEquippableSwappable(true);
    }

    public boolean isEquippableSwappable() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.isSwappable();
        }

        return false;
    }

    public RoseItem setEquippableDamageOnHurt(boolean damageOnHurt) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            EquippableComponent component = this.getEquippableComponent();
            component.setDamageOnHurt(damageOnHurt);
            this.setEquippableComponent(component);
        }

        return this;
    }

    public RoseItem setEquippableDamageOnHurt() {
        return this.setEquippableDamageOnHurt(true);
    }

    public boolean isEquippableDamageOnHurt() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasEquippableComponent()) {
            EquippableComponent component = this.getEquippableComponent();
            return component.isDamageOnHurt();
        }

        return false;
    }

    // Firework Meta

    public RoseItem setFireworkEffect(FireworkEffect effect) {
        if (this.meta instanceof FireworkEffectMeta)
            ((FireworkEffectMeta) this.meta).setEffect(effect);
        else if (this.meta instanceof FireworkMeta)
            ((FireworkMeta) this.meta).addEffect(effect);

        return this;
    }

    public RoseItem addFireworkEffect(FireworkEffect effect) {
        return this.setFireworkEffect(effect);
    }

    public RoseItem addFireworkEffects(FireworkEffect... effects) {
        if (this.meta instanceof FireworkEffectMeta)
            ((FireworkEffectMeta) this.meta).setEffect(effects[0]);
        else if (this.meta instanceof FireworkMeta)
            ((FireworkMeta) this.meta).addEffects(effects);

        return this;
    }

    public RoseItem addFireworkEffects(List<FireworkEffect> effects) {
        if (this.meta instanceof FireworkEffectMeta && !effects.isEmpty())
            this.addFireworkEffect(effects.get(0));
        else if (this.meta instanceof FireworkMeta && !effects.isEmpty())
            for (FireworkEffect effect : effects)
                this.addFireworkEffect(effect);

        return this;
    }

    public RoseItem setFireworkEffects(List<FireworkEffect> effects) {
        this.clearFireworkEffects();
        return this.addFireworkEffects(effects);
    }

    public RoseItem clearFireworkEffects() {
        if (this.meta instanceof FireworkMeta)
            ((FireworkMeta) this.meta).clearEffects();

        return this;
    }

    public RoseItem removeFireworkEffect(int index) {
        if (this.meta instanceof FireworkMeta)
            ((FireworkMeta) this.meta).removeEffect(index);

        return this;
    }

    public RoseItem setFireworkPower(int power) {
        if (this.meta instanceof FireworkMeta)
            ((FireworkMeta) this.meta).setPower(power);

        return this;
    }

    public FireworkEffect getFireworkEffect() {
        if (this.meta instanceof FireworkEffectMeta)
            return ((FireworkEffectMeta) this.meta).getEffect();

        return null;
    }

    public boolean hasFireworkEffect() {
        return this.meta instanceof FireworkEffectMeta && ((FireworkEffectMeta) this.meta).hasEffect();
    }

    public List<FireworkEffect> getFireworkEffects() {
        if (this.meta instanceof FireworkMeta)
            return ((FireworkMeta) this.meta).getEffects();

        if (this.meta instanceof FireworkEffectMeta)
            return Collections.singletonList(((FireworkEffectMeta) this.meta).getEffect());

        return null;
    }

    public int getFireworkEffectsSize() {
        if (this.meta instanceof FireworkMeta)
            return ((FireworkMeta) this.meta).getEffectsSize();

        return 0;
    }

    public int getFireworkPower() {
        if (this.meta instanceof FireworkMeta)
            return ((FireworkMeta) this.meta).getPower();

        return 0;
    }

    public boolean hasFireworkEffects() {
        return (this.meta instanceof FireworkMeta && ((FireworkMeta) this.meta).hasEffects()) || this.hasFireworkEffect();
    }

    // Food Component

    public <T> RoseItem setFoodComponent(T foodComponent) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setFood((FoodComponent) foodComponent);

        return this;
    }

    public <T> T getFoodComponent() {
        return NMSUtil.getVersionNumber() >= 21 ? (T) this.meta.getFood() : null;
    }

    public boolean hasFoodComponent() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasFood();
    }

    public RoseItem setFoodNutrition(int nutrition) {
        if (NMSUtil.getVersionNumber() >= 21) {
            FoodComponent component = this.getFoodComponent();
            component.setNutrition(nutrition);
            this.setFoodComponent(component);
        }

        return this;
    }

    public int getFoodNutrition() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasFoodComponent()) {
            FoodComponent component = this.getFoodComponent();
            return component.getNutrition();
        }

        return 0;
    }

    public RoseItem setFoodSaturation(float saturation) {
        if (NMSUtil.getVersionNumber() >= 21) {
            FoodComponent component = this.getFoodComponent();
            component.setSaturation(saturation);
            this.setFoodComponent(component);
        }

        return this;
    }

    public float getFoodSaturation() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasFoodComponent()) {
            FoodComponent component = this.getFoodComponent();
            return component.getSaturation();
        }

        return 0.0F;
    }

    public RoseItem setCanAlwaysEat(boolean canAlwaysEat) {
        if (NMSUtil.getVersionNumber() >= 21) {
            FoodComponent component = this.getFoodComponent();
            component.setCanAlwaysEat(canAlwaysEat);
            this.setFoodComponent(component);
        }

        return this;
    }

    public RoseItem setCanAlwaysEat() {
        return this.setCanAlwaysEat(true);
    }

    public boolean canAlwaysEat() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasFoodComponent()) {
            FoodComponent component = this.getFoodComponent();
            return component.canAlwaysEat();
        }

        return false;
    }

    // Jukebox Component

    public <T> RoseItem setJukeboxPlayableComponent(T jukeboxPlayableComponent) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setJukeboxPlayable((JukeboxPlayableComponent) jukeboxPlayableComponent);

        return this;
    }

    public <T> T getJukeboxPlayableComponent() {
        return NMSUtil.getVersionNumber() >= 21 ? (T) this.meta.getJukeboxPlayable() : null;
    }

    public boolean hasJukeboxPlayableComponent() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasJukeboxPlayable();
    }

    public RoseItem setJukeboxSong(NamespacedKey key) {
        if (NMSUtil.getVersionNumber() >= 21) {
            JukeboxPlayableComponent component = this.getJukeboxPlayableComponent();
            component.setSongKey(key);
            this.setJukeboxPlayableComponent(component);
        }

        return this;
    }

    public NamespacedKey getJukeboxSong() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasJukeboxPlayableComponent()) {
            JukeboxPlayableComponent component = this.getJukeboxPlayableComponent();
            return component.getSongKey();
        }

        return null;
    }

    public RoseItem setShowJukeboxSongInTooltip(boolean showInTooltip) {
        if (NMSUtil.getVersionNumber() >= 21) {
            JukeboxPlayableComponent component = this.getJukeboxPlayableComponent();
            component.setShowInTooltip(showInTooltip);
            this.setJukeboxPlayableComponent(component);
        }

        return this;
    }

    public RoseItem setShowJukeboxSongInTooltip() {
        return this.setShowJukeboxSongInTooltip(true);
    }

    public boolean isShowingJukeboxSongInTooltip() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasJukeboxPlayableComponent()) {
            JukeboxPlayableComponent component = this.getJukeboxPlayableComponent();
            return component.isShowInTooltip();
        }

        return false;
    }

    // Knowledge Book Meta

    public RoseItem addKnowledgeBookRecipe(NamespacedKey... recipes) {
        if (this.meta instanceof KnowledgeBookMeta)
            ((KnowledgeBookMeta) this.meta).addRecipe(recipes);

        return this;
    }

    public RoseItem setKnowledgeBookRecipes(List<NamespacedKey> recipes) {
        if (this.meta instanceof KnowledgeBookMeta)
            ((KnowledgeBookMeta) this.meta).setRecipes(recipes);

        return this;
    }

    public List<NamespacedKey> getKnowledgeBookRecipes() {
        if (this.meta instanceof KnowledgeBookMeta)
            return ((KnowledgeBookMeta) this.meta).getRecipes();

        return null;
    }

    public boolean hasKnowledgeBookRecipes() {
        return this.meta instanceof KnowledgeBookMeta && ((KnowledgeBookMeta) this.meta).hasRecipes();
    }

    // Color Meta

    public RoseItem setColor(Color color) {
        if (this.meta instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) this.meta).setColor(color);
        else if (this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).setColor(color);
        else if (this.meta instanceof MapMeta)
            ((MapMeta) this.meta).setColor(color);

        return this;
    }

    public Color getColor() {
        if (this.meta instanceof LeatherArmorMeta)
            return ((LeatherArmorMeta) this.meta).getColor();
        else if (this.meta instanceof PotionMeta)
            return ((PotionMeta) this.meta).getColor();
        else if (this.meta instanceof MapMeta)
            return ((MapMeta) this.meta).getColor();

        return null;
    }

    public boolean hasColor() {
        if (this.meta instanceof PotionMeta)
            return ((PotionMeta) this.meta).hasColor();
        else if (this.meta instanceof MapMeta)
            return ((MapMeta) this.meta).hasColor();

        return false;
    }

    // Map Meta

    public RoseItem setMapLocationName(String name) {
        if (this.meta instanceof MapMeta)
            ((MapMeta) this.meta).setLocationName(name);

        return this;
    }

    public RoseItem setMapId(int id) {
        if (this.meta instanceof MapMeta)
            ((MapMeta) this.meta).setMapId(id);

        return this;
    }

    public RoseItem setMapView(MapView map) {
        if (this.meta instanceof MapMeta)
            ((MapMeta) this.meta).setMapView(map);

        return this;
    }

    public RoseItem setMapScaling(boolean value) {
        if (this.meta instanceof MapMeta)
            ((MapMeta) this.meta).setScaling(value);

        return this;
    }

    public RoseItem setMapScale(MapView.Scale scale) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setScale(scale);
        }

        return this;
    }

    public RoseItem setMapCentre(int x, int z) {
        this.setMapCentreX(x);
        return this.setMapCentreZ(z);
    }

    public RoseItem setMapCentreX(int x) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setCenterX(x);
        }

        return this;
    }

    public RoseItem setMapCentreZ(int z) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setCenterZ(z);
        }

        return this;
    }

    public RoseItem setMapWorld(World world) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setWorld(world);
        }

        return this;
    }

    public RoseItem setMapTracking() {
        return this.setMapTracking(true);
    }

    public RoseItem setMapTracking(boolean tracking) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setTrackingPosition(tracking);
        }

        return this;
    }

    public RoseItem setMapTrackingUnlimited() {
        return this.setMapTrackingUnlimited(true);
    }

    public RoseItem setMapTrackingUnlimited(boolean unlimited) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setUnlimitedTracking(unlimited);
        }

        return this;
    }

    public RoseItem setMapLocked() {
        return this.setMapLocked(true);
    }

    public RoseItem setMapLocked(boolean locked) {
        if (this.meta instanceof MapMeta) {
            MapMeta meta = (MapMeta) this.meta;
            if (!meta.hasMapView())
                return this;

            MapView view = meta.getMapView();
            if (view != null)
                view.setLocked(locked);
        }

        return this;
    }

    public String getMapLocationName() {
        if (this.meta instanceof MapMeta)
            return ((MapMeta) this.meta).getLocationName();

        return null;
    }

    public int getMapId() {
        if (this.meta instanceof MapMeta)
            return ((MapMeta) this.meta).getMapId();

        return -1;
    }

    public MapView getMapView() {
        if (this.meta instanceof MapMeta)
            return ((MapMeta) this.meta).getMapView();

        return null;
    }

    public MapView.Scale getMapScale() {
        MapView view = this.getMapView();
        if (view != null)
            return view.getScale();

        return null;
    }

    public Integer getMapCentreX() {
        MapView view = this.getMapView();
        if (view != null)
            return view.getCenterX();

        return null;
    }

    public Integer getMapCentreZ() {
        MapView view = this.getMapView();
        if (view != null)
            return view.getCenterZ();

        return null;
    }

    public World getMapWorld() {
        MapView view = this.getMapView();
        if (view != null)
            return view.getWorld();

        return null;
    }

    public boolean hasMapLocationName() {
        return this.meta instanceof MapMeta && ((MapMeta) this.meta).hasLocationName();
    }

    public boolean hasMapView() {
        return this.meta instanceof MapMeta && ((MapMeta) this.meta).hasMapView();
    }

    public boolean isScaling() {
        return this.meta instanceof MapMeta && ((MapMeta) this.meta).isScaling();
    }

    public boolean isMapTracking() {
        MapView view = this.getMapView();
        if (view != null)
            return view.isTrackingPosition();

        return false;
    }

    public boolean isMapTrackingUnlimited() {
        MapView view = this.getMapView();
        if (view != null)
            return view.isUnlimitedTracking();

        return false;
    }

    public boolean isMapLocked() {
        MapView view = this.getMapView();
        if (view != null)
            return view.isLocked();

        return false;
    }

    // Music Instrument Meta

    public <T> T getMusicInstrument() {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof MusicInstrumentMeta)
            return (T) ((MusicInstrumentMeta) this.meta).getInstrument();

        return null;
    }

    public <T> RoseItem setMusicInstrument(T instrument) {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof MusicInstrumentMeta)
            ((MusicInstrumentMeta) this.meta).setInstrument((MusicInstrument) instrument);

        return this;
    }

    // Ominous Bottle Meta

    public int getOminousBottleAmplifier() {
        if (NMSUtil.getVersionNumber() >= 21 && this.meta instanceof OminousBottleMeta)
            return ((OminousBottleMeta) this.meta).getAmplifier();

        return 0;
    }

    public boolean hasOminousBottleAmplifier() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta instanceof OminousBottleMeta && ((OminousBottleMeta) this.meta).hasAmplifier();
    }

    // Potion Meta

    public RoseItem addPotionEffect(PotionEffect effect, boolean overwrite) {
        if (this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).addCustomEffect(effect, overwrite);
        else if (this.meta instanceof SuspiciousStewMeta)
            ((SuspiciousStewMeta) this.meta).addCustomEffect(effect, overwrite);

        return this;
    }

    public RoseItem addPotionEffects(List<PotionEffect> effects) {
        if (this.meta instanceof PotionMeta || this.meta instanceof SuspiciousStewMeta)
            for (PotionEffect effect : effects)
                this.addPotionEffect(effect, true);

        return this;
    }

    public RoseItem setPotionEffects(List<PotionEffect> effects) {
        this.clearPotionEffects();
        return this.addPotionEffects(effects);
    }

    public RoseItem clearPotionEffects() {
        if (this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).clearCustomEffects();
        else if (this.meta instanceof SuspiciousStewMeta)
            ((SuspiciousStewMeta) this.meta).clearCustomEffects();

        return this;
    }

    public RoseItem removePotionEffect(PotionEffectType type) {
        if (this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).removeCustomEffect(type);
        else if (this.meta instanceof SuspiciousStewMeta)
            ((SuspiciousStewMeta) this.meta).clearCustomEffects();

        return this;
    }

    public RoseItem setPotionData(PotionData data) {
        if (this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).setBasePotionData(data);

        return this;
    }

    public RoseItem setPotionType(PotionType type) {
        if (NMSUtil.getVersionNumber() >= 20 && this.meta instanceof PotionMeta)
            ((PotionMeta) this.meta).setBasePotionType(type);

        return this;
    }

    public PotionData getBasePotionData() {
        if (this.meta instanceof PotionMeta)
            return ((PotionMeta) this.meta).getBasePotionData();

        return null;
    }

    public List<PotionEffect> getCustomPotionEffects() {
        if (this.meta instanceof PotionMeta)
            return ((PotionMeta) this.meta).getCustomEffects();
        else if (this.meta instanceof SuspiciousStewMeta)
            return ((SuspiciousStewMeta) this.meta).getCustomEffects();

        return null;
    }

    public boolean hasCustomEffect(PotionEffectType type) {
        return (this.meta instanceof PotionMeta && ((PotionMeta) this.meta).hasCustomEffect(type))
                || (this.meta instanceof SuspiciousStewMeta && ((SuspiciousStewMeta) this.meta).hasCustomEffect(type));
    }

    public boolean hasCustomPotionEffects() {
        return (this.meta instanceof PotionMeta && ((PotionMeta) this.meta).hasCustomEffects())
                || (this.meta instanceof SuspiciousStewMeta && ((SuspiciousStewMeta) this.meta).hasCustomEffects());
    }

    public PotionType getBasePotionType() {
        if (this.meta instanceof PotionMeta)
            return NMSUtil.getVersionNumber() >= 20
                    ? ((PotionMeta) this.meta).getBasePotionType() : this.getBasePotionData().getType();

        return null;
    }

    public boolean hasBasePotionType() {
        return this.meta instanceof PotionMeta && NMSUtil.getVersionNumber() >= 20 && ((PotionMeta) this.meta).hasBasePotionType();
    }

    // Repairable Meta

    public RoseItem setRepairCost(int cost) {
        if (NMSUtil.getVersionNumber() >= 18 && this.meta instanceof Repairable)
            ((Repairable) this.meta).setRepairCost(cost);

        return this;
    }

    public int getRepairCost() {
        if (NMSUtil.getVersionNumber() >= 18 && this.meta instanceof Repairable)
            return ((Repairable) this.meta).getRepairCost();

        return 0;
    }

    public boolean hasRepairCost() {
        return NMSUtil.getVersionNumber() >= 18 && this.meta instanceof Repairable && ((Repairable) this.meta).hasRepairCost();
    }

    // Ominous Bottle Meta

    public RoseItem setOminousBottleAmplifier(int amplifier) {
        if (NMSUtil.getVersionNumber() >= 21 && this.meta instanceof OminousBottleMeta)
            ((OminousBottleMeta) this.meta).setAmplifier(amplifier);

        return this;
    }

    // Skull Meta

    public RoseItem setSkullOwner(String owner) {
        if (this.meta instanceof SkullMeta)
            ((SkullMeta) this.meta).setOwner(owner);

        return this;
    }

    public boolean hasSkullOwner() {
        return (this.meta instanceof SkullMeta && ((SkullMeta) this.meta).getOwner() != null);
    }

    public RoseItem setSkullOwner(OfflinePlayer owner) {
        if (this.meta instanceof SkullMeta)
            ((SkullMeta) this.meta).setOwningPlayer(owner);

        return this;
    }

    public <T> RoseItem setSkullOwnerProfile(T profile) {
        if (NMSUtil.getVersionNumber() >= 18 && this.meta instanceof SkullMeta)
            ((SkullMeta) this.meta).setOwnerProfile((PlayerProfile) profile);

        return this;
    }

    public RoseItem setSkullSound(NamespacedKey sound) {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof SkullMeta)
            ((SkullMeta) this.meta).setNoteBlockSound(sound);

        return this;
    }

    public RoseItem setSkullTexture(String texture) {
        if (!(this.meta instanceof SkullMeta))
            return this;

        SkullMeta skullMeta = (SkullMeta) this.meta;

        if (NMSUtil.getVersionNumber() >= 18) {
            if (NMSUtil.isPaper())
                return this.setPaperSkullTexture(texture);

            PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "");
            PlayerTextures textures = profile.getTextures();

            String decodedTextureJson = new String(Base64.getDecoder().decode(texture));
            String decodedTextureUrl = decodedTextureJson.substring(28, decodedTextureJson.length() - 4);

            try {
                textures.setSkin(new URL(decodedTextureUrl));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            profile.setTextures(textures);
            skullMeta.setOwnerProfile(profile);
            return this;
        }

        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "");
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            if (this.setSkullMetaProfileMethod == null) {
                this.setSkullMetaProfileMethod = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                this.setSkullMetaProfileMethod.setAccessible(true);
            }

            this.setSkullMetaProfileMethod.invoke(skullMeta, profile);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return this;
    }

    private RoseItem setPaperSkullTexture(String texture) {
        com.destroystokyo.paper.profile.PlayerProfile profile = Bukkit.createProfile(UUID.nameUUIDFromBytes(texture.getBytes()), "");
        PlayerTextures textures = profile.getTextures();

        String decodedTextureJson = new String(Base64.getDecoder().decode(texture));
        String decodedTextureUrl = decodedTextureJson.substring(28, decodedTextureJson.length() - 4);

        try {
            textures.setSkin(new URL(decodedTextureUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        profile.setTextures(textures);
        ((SkullMeta) this.meta).setPlayerProfile(profile);
        return this;
    }

    public String getSkullOwner() {
        if (this.meta instanceof SkullMeta)
            return ((SkullMeta) this.meta).getOwner();

        return null;
    }

    public OfflinePlayer getSkullOwningPlayer() {
        if (this.meta instanceof SkullMeta)
            return ((SkullMeta) this.meta).getOwningPlayer();

        return null;
    }

    public <T> T getOwnerProfile() {
        if (NMSUtil.getVersionNumber() >= 18 && this.meta instanceof SkullMeta)
            return (T) ((SkullMeta) this.meta).getOwnerProfile();

        return null;
    }

    public String getSkullTexture() {
        if (NMSUtil.getVersionNumber() >= 18 && this.meta instanceof SkullMeta) {
            PlayerProfile profile = ((SkullMeta) this.meta).getOwnerProfile();
            if (profile == null || profile.getTextures().isEmpty())
                return null;

            URL skinUrl = profile.getTextures().getSkin();
            if (skinUrl == null)
                return null;

            String texturesStr = "{\"textures\":{\"SKIN\":{\"url\":\"" + skinUrl + "\"}}}";
            return Base64.getEncoder().encodeToString(texturesStr.getBytes());
        }

        return null;
    }

    public NamespacedKey getSkullSound() {
        if (NMSUtil.getVersionNumber() >= 19 && this.meta instanceof SkullMeta)
            return ((SkullMeta) this.meta).getNoteBlockSound();

        return null;
    }

    // Enchantment Storage

    public RoseItem addStoredEnchantment(Enchantment enchantment, int level) {
        if (this.meta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta) this.meta).addStoredEnchant(enchantment, level, true);

        return this;
    }

    public RoseItem addStoredEnchantments(Map<Enchantment, Integer> enchantments) {
        if (this.meta instanceof EnchantmentStorageMeta)
            for (Enchantment enchantment : enchantments.keySet())
                this.addStoredEnchantment(enchantment, enchantments.get(enchantment));

        return this;
    }

    public RoseItem setStoredEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : this.getStoredEnchantments().keySet())
            this.removeStoredEnchantment(enchantment);

        return this.addStoredEnchantments(enchantments);
    }

    public RoseItem removeStoredEnchantment(Enchantment enchantment) {
        if (this.meta instanceof EnchantmentStorageMeta)
            ((EnchantmentStorageMeta) this.meta).removeStoredEnchant(enchantment);

        return this;
    }

    public int getStoredEnchantmentLevel(Enchantment enchantment) {
        if (this.meta instanceof EnchantmentStorageMeta)
            return ((EnchantmentStorageMeta) this.meta).getStoredEnchantLevel(enchantment);

        return 0;
    }

    public Map<Enchantment, Integer> getStoredEnchantments() {
        if (this.meta instanceof EnchantmentStorageMeta)
            return ((EnchantmentStorageMeta) this.meta).getStoredEnchants();

        return null;
    }

    public boolean hasConflictingStoredEnchantment(Enchantment enchantment) {
        return this.meta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) this.meta).hasConflictingStoredEnchant(enchantment);
    }

    public boolean hasStoredEnchantment(Enchantment enchantment) {
        return this.meta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) this.meta).hasStoredEnchant(enchantment);
    }

    public boolean hasStoredEnchantments() {
        return this.meta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta) this.meta).hasStoredEnchants();
    }

    // Tropical Fish Bucket Meta

    public RoseItem setTropicalFishBodyColor(DyeColor color) {
        if (this.meta instanceof TropicalFishBucketMeta)
            ((TropicalFishBucketMeta) this.meta).setBodyColor(color);

        return this;
    }

    public RoseItem setTropicalFishPattern(TropicalFish.Pattern pattern) {
        if (this.meta instanceof TropicalFishBucketMeta)
            ((TropicalFishBucketMeta) this.meta).setPattern(pattern);

        return this;
    }

    public RoseItem setTropicalFishPatternColor(DyeColor color) {
        if (this.meta instanceof TropicalFishBucketMeta)
            ((TropicalFishBucketMeta) this.meta).setPatternColor(color);

        return this;
    }

    public DyeColor getTropicalFishBodyColor() {
        if (this.meta instanceof TropicalFishBucketMeta)
            return ((TropicalFishBucketMeta) this.meta).getBodyColor();

        return null;
    }

    public TropicalFish.Pattern getTropicalFishPattern() {
        if (this.meta instanceof TropicalFishBucketMeta)
            return ((TropicalFishBucketMeta) this.meta).getPattern();

        return null;
    }

    public DyeColor getTropicalFishPatternColor() {
        if (this.meta instanceof TropicalFishBucketMeta)
            return ((TropicalFishBucketMeta) this.meta).getPatternColor();

        return null;
    }

    public boolean hasTropicalFishVariant() {
        return this.meta instanceof TropicalFishBucketMeta && ((TropicalFishBucketMeta) this.meta).hasVariant();
    }

    // Use Cooldown Component

    public <T> T getUseCooldownComponent() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3?
                (T) this.meta.getUseCooldown() : null;
    }

    public <T> RoseItem setUseCooldownComponent(T useCooldownComponent) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3)
            this.meta.setUseCooldown((UseCooldownComponent) useCooldownComponent);

        return this;
    }

    public RoseItem setUseCooldownSeconds(float seconds) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            UseCooldownComponent component = this.getUseCooldownComponent();
            component.setCooldownSeconds(seconds);
            this.setUseCooldownComponent(component);
        }

        return this;
    }

    public float getUseCooldownSeconds() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasUseCooldown()) {
            UseCooldownComponent component = this.getUseCooldownComponent();
            return component.getCooldownSeconds();
        }

        return 0.0F;
    }

    public RoseItem setUseCooldownGroup(NamespacedKey group) {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3) {
            UseCooldownComponent component = this.getUseCooldownComponent();
            component.setCooldownGroup(group);
            this.setUseCooldownComponent(component);
        }

        return this;
    }

    public NamespacedKey getUseCooldownGroup() {
        if (NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.hasUseCooldown()) {
            UseCooldownComponent component = this.getUseCooldownComponent();
            return component.getCooldownGroup();
        }

        return null;
    }

    public boolean hasUseCooldown() {
        return NMSUtil.getVersionNumber() >= 21 && NMSUtil.getMinorVersionNumber() >= 3 && this.meta.hasUseCooldown();
    }

    // Rarity Component

    public <T> RoseItem setRarity(T rarity) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setRarity((ItemRarity) rarity);

        return this;
    }

    public <T> T getRarity() {
        return NMSUtil.getVersionNumber() >= 21 ? (T) this.meta.getRarity() : null;
    }

    public boolean hasRarity() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasRarity();
    }

    // Tool Component

    public <T> RoseItem setToolComponent(T toolComponent) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setTool((ToolComponent) toolComponent);

        return this;
    }

    public <T> T getToolComponent() {
        return NMSUtil.getVersionNumber() >= 21 ? (T) this.meta.getTool() : null;
    }

    public boolean hasToolComponent() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasTool();
    }

    public RoseItem setToolMiningSpeed(float speed) {
        if (NMSUtil.getVersionNumber() >= 21) {
            ToolComponent component = this.getToolComponent();
            component.setDefaultMiningSpeed(speed);
            this.setToolComponent(component);
        }

        return this;
    }

    public Float getToolMiningSpeed() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasToolComponent()) {
            ToolComponent component = this.getToolComponent();
            return component.getDefaultMiningSpeed();
        }

        return null;
    }

    public RoseItem setToolDamagePerBlock(int damage) {
        if (NMSUtil.getVersionNumber() >= 21) {
            ToolComponent component = this.getToolComponent();
            component.setDamagePerBlock(damage);
            this.setToolComponent(component);
        }

        return this;
    }

    public Integer getToolDamagePerBlock() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasToolComponent()) {
            ToolComponent component = this.getToolComponent();
            return component.getDamagePerBlock();
        }

        return null;
    }

    public RoseItem addToolRule(Material block, Float speed, Boolean correctForDrops) {
        if (NMSUtil.getVersionNumber() >= 21) {
            ToolComponent component = this.getToolComponent();
            component.addRule(block, speed, correctForDrops);
            this.setToolComponent(component);
        }

        return this;
    }

    public <T> List<T> getToolRules() {
        if (NMSUtil.getVersionNumber() >= 21 && this.hasToolComponent()) {
            ToolComponent component = this.getToolComponent();
            return (List<T>) component.getRules();
        }

        return null;
    }

    // Stack Size

    public RoseItem setMaxStackSize(Integer max) {
        if (NMSUtil.getVersionNumber() >= 21)
            this.meta.setMaxStackSize(max);

        return this;
    }

    public int getMaxStackSize() {
        return NMSUtil.getVersionNumber() >= 21 ? this.meta.getMaxStackSize() : this.stack.getMaxStackSize();
    }

    public boolean hasMaxStackSize() {
        return NMSUtil.getVersionNumber() >= 21 && this.meta.hasMaxStackSize();
    }

    // Misc Methods

    public PersistentDataContainer getPersistentDataContainer() {
        return this.meta.getPersistentDataContainer();
    }

    public RoseItem copy() {
        RoseItem item = new RoseItem(this.stack.clone(), this.meta.clone());
        item.isEmpty = this.isEmpty;
        return item;
    }

    public boolean isSimilar(RoseItem item) {
        if (this.isEmpty && item.isEmpty)
            return true;

        return this.stack.isSimilar(item.stack);
    }

    public String getAsComponentString() {
        return this.meta.getAsComponentString();
    }

    public String getAsString() {
        return this.meta.getAsString();
    }

    // Serialization

    public static RoseItem deserialize(ConfigurationSection section) {
        return ItemSerializer.deserialize(section);
    }

    public void serialize(ConfigurationSection section) {
        ItemSerializer.serialize(this, section);
    }

    /**
     * Merges the two items together, overwriting this item and taking new properties from the param.
     * @param item The {@link RoseItem} to use.
     * @return The merged item.
     */
    public RoseItem mergeWith(RoseItem item) {
        return ItemMerger.merge(this, item);
    }

    // Static Methods

    public static RoseItem empty() {
        // Set as stone to avoid issues when placing the item in a menu.
        RoseItem item = new RoseItem(Material.STONE);
        item.isEmpty = true;

        return item;
    }

    public static RoseItem of(Material material) {
        return new RoseItem(material);
    }

    public static RoseItem of(Material material, String name) {
        return new RoseItem(material).setDisplayName(name);
    }

    @Override
    public RoseItem get(Context context) {
        return this;
    }

}
