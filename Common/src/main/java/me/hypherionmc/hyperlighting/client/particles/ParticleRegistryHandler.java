package me.hypherionmc.hyperlighting.client.particles;

import me.hypherionmc.hyperlighting.common.init.FlameParticles;
import me.hypherionmc.hyperlighting.common.init.HLParticles;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class ParticleRegistryHandler {

    public static void registerParticles(ParticleStrategy strategy) {
        for (FlameParticles value : FlameParticles.values()) {
           strategy.register(value.getParticle().get(), ColoredFlameParticle.Factory::new);
        }
    }

    public interface ParticleStrategy {
        <T extends ParticleOptions> void register(ParticleType<T> particle, ParticleEngine.SpriteParticleRegistration<T> provider);
    }

}
