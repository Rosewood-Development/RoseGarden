package dev.rosewood.rosegarden.compatibility.handler;

import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Shearable;

public class CurrentShearedHandler implements ShearedHandler {

    @Override
    public boolean isSheared(LivingEntity shearable) {
        if (NMSUtil.isPaper() && shearable instanceof io.papermc.paper.entity.Shearable)
            return !((io.papermc.paper.entity.Shearable) shearable).readyToBeSheared();
        if (shearable instanceof Shearable)
            return ((Shearable) shearable).isSheared();
        return false;
    }

}
