package dev.rosewood.rosegarden.compatibility.handler;

import dev.rosewood.rosegarden.compatibility.wrapper.WrappedKeyed;
import org.bukkit.entity.Villager;

public interface OldEnumHandler {

    WrappedKeyed getProfession(Villager villager);

}
