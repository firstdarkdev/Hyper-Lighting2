package me.hypherionmc.hyperlighting.client;

import me.hypherionmc.hyperlighting.client.model.NeonFlyModel;
import me.hypherionmc.hyperlighting.client.particles.ParticleRegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author HypherionSA
 * @date 03/07/2022
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        ParticleRegistryHandler.registerParticles(Minecraft.getInstance().particleEngine::register);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NeonFlyModel.LAYER_LOCATION, NeonFlyModel::createBodyLayer);
    }

}
