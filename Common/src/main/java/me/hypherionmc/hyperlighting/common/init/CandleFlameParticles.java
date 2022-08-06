package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.common.particles.WrappedSimpleParticleType;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.DyeColor;

import java.util.Arrays;

import static me.hypherionmc.hyperlighting.common.init.HLParticles.register;

/**
 * @author HypherionSA
 * @date 05/08/2022
 */
public enum CandleFlameParticles {
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

    private CandleFlameParticles(DyeColor color) {
        PARTICLE = register("cflame_" + color.getName().toLowerCase(), () -> new WrappedSimpleParticleType(false));
        this.color = color;
    }

    public RegistryObject<SimpleParticleType> getParticle() {
        return PARTICLE;
    }

    public static RegistryObject<SimpleParticleType> getParticleByColor(DyeColor color) {
        return Arrays.stream(CandleFlameParticles.values()).filter(p -> p.color == color).findFirst().get().getParticle();
    }

    public static void loadAll() {}
}
