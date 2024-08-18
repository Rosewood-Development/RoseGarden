package dev.rosewood.rosegarden.compatibility.handler;

import dev.rosewood.rosegarden.compatibility.wrapper.LegacyWrappedKeyed;
import dev.rosewood.rosegarden.compatibility.wrapper.WrappedKeyed;
import org.bukkit.entity.Villager;

public class LegacyOldEnumHandler implements OldEnumHandler {

    @Override
    public WrappedKeyed getProfession(Villager villager) {
        return new LegacyWrappedKeyed(villager.getProfession());
    }

}
