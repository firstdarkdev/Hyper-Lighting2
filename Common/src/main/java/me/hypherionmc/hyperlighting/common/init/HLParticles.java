package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.common.particles.WrappedSimpleParticleType;
import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class HLParticles {

    public static final RegistrationProvider<ParticleType<?>> PARTICLES = RegistrationProvider.get(Registry.PARTICLE_TYPE_REGISTRY, Constants.MOD_ID);

    public static RegistryObject<SimpleParticleType> COLORED_FLAME = register("colored_flame", () -> new WrappedSimpleParticleType(false));

    public static <T extends ParticleType<?>> RegistryObject<T> register(String name, Supplier<T> particle) {
        return PARTICLES.register(name, particle);
    }

    public static void loadAll() {}

}
