package dev.rosewood.rosegarden.compatibility.handler;

import dev.rosewood.rosegarden.utils.NMSUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Shearable;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Snowman;

@SuppressWarnings("removal")
public class CurrentShearedHandler implements ShearedHandler {

    @Override
    public boolean isSheared(LivingEntity shearable) {
        if (NMSUtil.isPaper() && shearable instanceof io.papermc.paper.entity.Shearable)
            return !((io.papermc.paper.entity.Shearable) shearable).readyToBeSheared();
        if (shearable instanceof Shearable)
            return ((Shearable) shearable).isSheared();
        if (shearable instanceof Snowman)
            return ((Snowman) shearable).isDerp();
        return false;
    }

    @Override
    public void setSheared(LivingEntity shearable, boolean sheared) {
        if (shearable instanceof Sheep) {
            if (NMSUtil.isPaper()) {
                ((Sheep) shearable).setSheared(sheared);
            } else {
                ((Shearable) shearable).setSheared(sheared);
            }
        } else if (shearable instanceof Shearable) {
            ((Shearable) shearable).setSheared(sheared);
        } else if (shearable instanceof Snowman) {
            ((Snowman) shearable).setDerp(sheared);
        }
    }

}
