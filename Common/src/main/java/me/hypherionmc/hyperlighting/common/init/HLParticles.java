package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class HLParticles {

    public static final RegistrationProvider<ParticleType<?>> PARTICLES = RegistrationProvider.get(BuiltInRegistries.PARTICLE_TYPE, Constants.MOD_ID);

    public static <T extends ParticleType<?>> RegistryObject<T> register(String name, Supplier<T> particle) {
        return PARTICLES.register(name, particle);
    }

    public static void loadAll() {
        FlameParticles.loadAll();
        CandleFlameParticles.loadAll();
    }

}
