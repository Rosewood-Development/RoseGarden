package dev.rosewood.rosegarden.compatibility.handler;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CurrentCreatureSpawnerHandler implements CreatureSpawnerHandler {

    @Override
    public EntityType getSpawnedType(CreatureSpawner creatureSpawner) {
        return creatureSpawner.getSpawnedType();
    }

}
