package me.hypherionmc.hyperlighting.common.worldgen;

import me.hypherionmc.hyperlighting.common.init.HLEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;

/**
 * @author HypherionSA
 * @date 06/08/2022
 */
public class FabricWorldGen {

    public static void registerAll() {
        addNeonFlyGen();
    }

    private static void addNeonFlyGen() {
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.SWAMP),
                MobCategory.AMBIENT,
                HLEntities.NEONFLY.get(),
                10,
                8,
                25
        );
    }

}
