package me.hypherionmc.hyperlighting.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ColoredFlameParticle extends RisingParticle  {

    public ColoredFlameParticle(ClientLevel level, double x, double y, double z, double r, double g, double b) {
        super(level, x, y, z, 0, 0, 0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().inflate(dx, dy, dz));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float tickDelta) {
        float f = ((float) this.age + tickDelta) / (float) this.lifetime;
        return this.quadSize * (1.0f - f * f * 0.5f);
    }

    @Override
    protected int getLightColor(float tint) {
        float f = ((float) this.age + tint) / (float) this.lifetime;
        f = Mth.clamp(f, 0.0f, 1.0f);
        int i = super.getLightColor(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int) (f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    public static class SmallFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public SmallFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double x, double y, double z, double r, double g, double b) {
            ColoredFlameParticle coloredFlameParticle = new ColoredFlameParticle(clientLevel, x, y, z, r, g, b);
            coloredFlameParticle.setSpriteFromAge(this.spriteSet);
            coloredFlameParticle.scale(0.5f);
            return coloredFlameParticle;
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel clientLevel, double x, double y, double z, double r, double g, double b) {
            ColoredFlameParticle coloredFlameParticle = new ColoredFlameParticle(clientLevel, x, y, z, r, g, b);
            coloredFlameParticle.setSpriteFromAge(this.spriteSet);
            return coloredFlameParticle;
        }
    }
}
