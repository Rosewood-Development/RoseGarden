package dev.rosewood.rosegarden.gui.item;

public class ItemMerger {

    /**
     * Merges two {@link RoseItem}s.
     * Any non-null properties from Item B will be placed on Item A, overwriting them in the process.
     * @param a The first item.
     * @param b The second item.
     * @return The merged item.
     */
    public static RoseItem merge(RoseItem a, RoseItem b) {
        if (b.getType() != null && !b.isEmpty()) {
            a.setType(b.getType());
            a.setEmpty(false);
        }

        if (b.getAmount() != 0)
            a.setAmount(b.getAmount());

        if (b.getMeta() == null)
            return a;

        if (b.hasCustomModelData())
            a.setCustomModelData(b.getCustomModelData());

        if (b.hasDisplayName())
            a.setDisplayName(b.getDisplayName());

        if (b.hasLore())
            a.setLore(b.getLore());

        if (b.hasEnchantments())
            a.setEnchantments(b.getEnchantments());

        if (b.hasEnchantability())
            a.setEnchantability(b.getEnchantability());

        if (b.getDurability() > 0)
            a.setDurability(b.getDurability());

        if (b.getDamage() > 0)
            a.setDamage(b.getDamage());

        if (b.hasLocalizedName())
            a.setLocalizedName(b.getLocalizedName());

        if (b.isUnbreakable())
            a.setUnbreakable(b.isUnbreakable());

        if (b.hasEnchantmentGlint())
            a.setEnchantmentGlint(b.getEnchantmentGlint());

        if (b.isFireResistant())
            a.setFireResistant(b.isFireResistant());

        if (b.isGlider())
            a.setGlider(b.isGlider());

        if (!b.hasTooltip())
            a.hideTooltip();

        if (b.hasItemModel())
            a.setItemModel(b.getItemModel());

        if (b.hasItemName())
            a.setItemName(b.getItemName());

        if (b.hasTooltipStyle())
            a.setTooltipStyle(b.getTooltipStyle());

        if (!b.getItemFlags().isEmpty())
            a.addItemFlags(b.getItemFlags());

        if (b.hasUseRemainder())
            a.setUseRemainder(b.getUseRemainder());

        if (b.hasArmorTrim())
            a.setArmorTrim(b.getArmorTrim());

        if (b.hasAttributeModifiers())
            a.setAttributeModifiers(b.getAttributeModifiers());

        if (b.hasAxolotlVariant())
            a.setAxolotlVariant(b.getAxolotlVariant());

        if (b.getBannerPatterns() != null)
            a.addBannerPatterns(b.getBannerPatterns());

        if (b.hasBlockData())
            a.setBlockData(b.getBlockData(b.getType()));

        if (b.hasBlockState())
            a.setBlockState(b.getBlockState());

        if (b.hasBookPages())
            a.setBookPages(b.getBookPages());

        if (b.hasBookAuthor())
            a.setBookAuthor(b.getBookAuthor());

        if (b.hasBookGeneration())
            a.setBookGeneration(b.getBookGeneration());

        if (b.hasBookTitle())
            a.setBookTitle(b.getBookTitle());

        if (b.hasBundleItems())
            a.setBundleItems(b.getBundleItems());

        if (b.hasLodestone() && b.isLodestoneTracked())
            a.setLodestoneTracked(b.isLodestoneTracked());

        if (b.hasLodestone())
            a.setLodestoneLocation(b.getLodestoneLocation());

        if (b.hasCrossbowProjectiles())
            a.setCrossbowProjectiles(b.getCrossbowProjectiles());

        if (b.hasDamageResistant())
            a.setDamageResistant(b.getDamageResistant());

        if (b.hasEquippableComponent())
            a.setEquippableComponent(b.getEquippableComponent());

        if (b.hasFireworkEffect())
            a.setFireworkEffect(b.getFireworkEffect());

        if (b.hasFireworkEffects())
            a.setFireworkEffects(b.getFireworkEffects());

        if (b.getFireworkPower() != 0)
            a.setFireworkPower(b.getFireworkPower());

        if (b.hasFoodComponent())
            a.setFoodComponent(b.getFoodComponent());

        if (b.hasJukeboxPlayableComponent())
            a.setJukeboxPlayableComponent(b.getJukeboxPlayableComponent());

        if (b.hasKnowledgeBookRecipes())
            a.setKnowledgeBookRecipes(b.getKnowledgeBookRecipes());

        if (b.hasColor())
            a.setColor(b.getColor());

        if (b.hasMapLocationName())
            a.setMapLocationName(b.getMapLocationName());

        if (b.getMapId() != -1)
            a.setMapId(b.getMapId());

        if (b.hasMapView())
            a.setMapView(b.getMapView());

        if (b.isScaling())
            a.setMapScaling(b.isScaling());

        if (b.getMusicInstrument() != null)
            a.setMusicInstrument(b.getMusicInstrument());

        if (b.hasOminousBottleAmplifier())
            a.setOminousBottleAmplifier(b.getOminousBottleAmplifier());

        if (b.hasCustomPotionEffects())
            a.setPotionEffects(b.getCustomPotionEffects());

        if (b.hasBasePotionType())
            a.setPotionType(b.getBasePotionType());

        if (b.hasRepairCost())
            a.setRepairCost(b.getRepairCost());

        if (b.hasSkullOwner())
            a.setSkullOwner(b.getSkullOwner());

        if (b.getSkullTexture() != null)
            a.setSkullTexture(b.getSkullTexture());

        if (b.getSkullSound() != null)
            a.setSkullSound(b.getSkullSound());

        if (b.hasStoredEnchantments())
            a.setStoredEnchantments(b.getStoredEnchantments());

        if (b.hasTropicalFishVariant())
            a.setTropicalFishPattern(b.getTropicalFishPattern());

        if (b.getTropicalFishBodyColor() != null)
            a.setTropicalFishBodyColor(b.getTropicalFishBodyColor());

        if (b.getTropicalFishPatternColor() != null)
            a.setTropicalFishPatternColor(b.getTropicalFishPatternColor());

        if (b.hasUseCooldown())
            a.setUseCooldownComponent(b.getUseCooldownComponent());

        if (b.hasRarity())
            a.setRarity(b.getRarity());

        if (b.hasToolComponent())
            a.setToolComponent(b.getToolComponent());

        if (b.hasMaxStackSize())
            a.setMaxStackSize(b.getMaxStackSize());

        return a;
    }

}
