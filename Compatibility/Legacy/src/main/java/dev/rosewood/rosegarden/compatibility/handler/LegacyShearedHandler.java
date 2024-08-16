package dev.rosewood.rosegarden.compatibility.handler;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;

public class LegacyShearedHandler implements ShearedHandler {

    @Override
    public boolean isSheared(LivingEntity shearable) {
        if (shearable instanceof Sheep) return ((Sheep) shearable).isSheared();
        if (shearable instanceof Snowman) return ((Snowman) shearable).isDerp();
        return false;
    }

}
