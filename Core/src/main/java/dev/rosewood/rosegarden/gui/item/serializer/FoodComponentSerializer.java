package dev.rosewood.rosegarden.gui.item.serializer;

import dev.rosewood.rosegarden.gui.item.MetaSerializer;
import dev.rosewood.rosegarden.gui.item.RoseItem;
import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.meta.components.FoodComponent;

@SuppressWarnings("UnstableApiUsage")
public class FoodComponentSerializer implements MetaSerializer {

    public static final String FOOD = "food";
    public static final String NUTRITION = FOOD + ".nutrition";
    public static final String SATURATION = FOOD + ".saturation";
    public static final String CAN_ALWAYS_EAT = FOOD + ".can-always-eat";

    @Override
    public void read(RoseItem item, ConfigurationSection section) {
        if (!section.contains(FOOD))
            return;

        if (NMSUtil.getVersionNumber() < 21) {
            Bukkit.getLogger().warning("The " + FOOD + " item meta is only available on 1.21+!");
            return;
        }

        Bukkit.getConsoleSender().sendMessage("Reading Food: " + item.getType());
        if (section.contains(NUTRITION))
            item.setFoodNutrition(section.getInt(NUTRITION));

        if (section.contains(SATURATION))
            item.setFoodSaturation((float) section.getDouble(SATURATION));

        if (section.contains(CAN_ALWAYS_EAT))
            item.setCanAlwaysEat(section.getBoolean(CAN_ALWAYS_EAT));
    }

    @Override
    public void write(RoseItem item, ConfigurationSection section) {
        if (!item.hasFoodComponent())
            return;

        FoodComponent foodComponent = item.getFoodComponent();

        section.set(NUTRITION, foodComponent.getNutrition());
        section.set(SATURATION, foodComponent.getSaturation());
        section.set(CAN_ALWAYS_EAT, foodComponent.canAlwaysEat());
    }

}
