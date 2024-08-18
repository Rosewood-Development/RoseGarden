package dev.rosewood.rosegarden.compatibility.handler;

import dev.rosewood.rosegarden.compatibility.wrapper.CurrentWrappedKeyed;
import dev.rosewood.rosegarden.compatibility.wrapper.WrappedKeyed;
import org.bukkit.entity.Villager;

public class CurrentOldEnumHandler implements OldEnumHandler {

    @Override
    public WrappedKeyed getProfession(Villager villager) {
        return new CurrentWrappedKeyed(villager.getProfession());
    }

}
