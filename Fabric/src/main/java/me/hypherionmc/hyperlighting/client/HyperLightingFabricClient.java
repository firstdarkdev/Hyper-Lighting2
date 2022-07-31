package me.hypherionmc.hyperlighting.client;

import me.hypherionmc.hyperlighting.client.init.ClientRegistration;
import me.hypherionmc.hyperlighting.client.particles.ParticleRegistryHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class HyperLightingFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new ClientRegistration().registerAll();
        // TODO: Move to CraterLib as an Event
        ParticleRegistryHandler.registerParticles(new ParticleRegistryHandler.ParticleStrategy() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> particle, ParticleEngine.SpriteParticleRegistration<T> provider) {
                ParticleFactoryRegistry.getInstance().register(particle, provider::create);
            }
        });
    }
}
