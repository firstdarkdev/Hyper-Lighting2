package me.hypherionmc.hyperlighting.common.worldgen.biomemodifiers;

import com.mojang.serialization.Codec;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static me.hypherionmc.hyperlighting.common.worldgen.ForgeWorldGen.NEONFLY_CODEC;


/**
 * @author HypherionSA
 * @date 05/08/2022
 */
public record NeonFlyBiomeModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Constants.MOD_ID, "neonfly_spawn_serializer"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Constants.MOD_ID);


    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {

    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return NEONFLY_CODEC.get();
    }
}
