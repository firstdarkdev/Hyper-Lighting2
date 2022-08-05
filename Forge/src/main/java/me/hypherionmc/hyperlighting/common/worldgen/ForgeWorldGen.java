package me.hypherionmc.hyperlighting.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.worldgen.biomemodifiers.NeonFlyBiomeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author HypherionSA
 * @date 05/08/2022
 */
public class ForgeWorldGen {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOMEMODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<Codec<NeonFlyBiomeModifier>> NEONFLY_CODEC = BIOMEMODIFIERS.register(Constants.MOD_ID, () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("biomes").forGetter(NeonFlyBiomeModifier::biomes),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(NeonFlyBiomeModifier::feature)
            ).apply(builder, NeonFlyBiomeModifier::new)));

    public static void registerAll(IEventBus bus) {
        BIOMEMODIFIERS.register(bus);
    }
}
