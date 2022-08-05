package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLEntities;
import me.hypherionmc.hyperlighting.common.worldgen.FabricWorldGen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class HyperLightingFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonRegistration.registerAll();
        FabricWorldGen.registerAll();

        FabricDefaultAttributeRegistry.register(HLEntities.NEONFLY.get(), NeonFlyEntity.prepareAttributes());
        SpawnPlacements.register(HLEntities.NEONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE, NeonFlyEntity::canSpawn);
    }
}
