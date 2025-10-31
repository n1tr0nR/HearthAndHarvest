package dev.nitron.hearth.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

public class BreathParticle extends DustParticleBase<BreathParticleOptions> {
    protected BreathParticle(
            ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, BreathParticleOptions dustParticleOptions, SpriteSet spriteSet
    ) {
        super(clientLevel, d, e, f, g, h, i, dustParticleOptions, spriteSet);
        Vector3f vector3f = dustParticleOptions.getColor();
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();
        this.setAlpha(0.5F);
    }

    @Override
    public Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<BreathParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(
                BreathParticleOptions dustParticleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, RandomSource randomSource
        ) {
            return new BreathParticle(clientLevel, d, e, f, g, h, i, dustParticleOptions, this.sprites);
        }
    }
}
