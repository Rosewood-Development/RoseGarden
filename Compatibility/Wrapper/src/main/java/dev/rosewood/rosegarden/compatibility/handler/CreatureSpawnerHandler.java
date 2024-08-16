package dev.rosewood.rosegarden.compatibility.handler;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public interface CreatureSpawnerHandler {

    EntityType getSpawnedType(CreatureSpawner creatureSpawner);

}
