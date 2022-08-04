package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.common.particles.WrappedSimpleParticleType;
import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MaterialColor;

import java.util.Arrays;
import java.util.function.Supplier;

import static me.hypherionmc.hyperlighting.common.init.HLParticles.register;

/**
 * @author HypherionSA
 * @date 05/08/2022
 */
public enum FlameParticles {
    WHITE(DyeColor.WHITE),
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    GRAY(DyeColor.GRAY),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED),
    BLACK(DyeColor.BLACK);

    private final RegistryObject<SimpleParticleType> PARTICLE;
    private final DyeColor color;

    private FlameParticles(DyeColor color) {
        PARTICLE = register("flame_" + color.getName().toLowerCase(), () -> new WrappedSimpleParticleType(false));
        this.color = color;
    }

    public RegistryObject<SimpleParticleType> getParticle() {
        return PARTICLE;
    }

    public static RegistryObject<SimpleParticleType> getParticleByColor(DyeColor color) {
        return Arrays.stream(FlameParticles.values()).filter(p -> p.color == color).findFirst().get().getParticle();
    }

    public static void loadAll() {}
}
