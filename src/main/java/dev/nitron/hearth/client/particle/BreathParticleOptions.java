package dev.nitron.hearth.client.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.nitron.hearth.client.HearthHarvestClient;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

public class BreathParticleOptions extends ScalableParticleOptionsBase {
    public static final MapCodec<BreathParticleOptions> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(dustParticleOptions -> dustParticleOptions.color),
                            SCALE.fieldOf("scale").forGetter(ScalableParticleOptionsBase::getScale)
                    )
                    .apply(instance, BreathParticleOptions::new)
    );
    private final int color;

    public BreathParticleOptions(int i, float f) {
        super(f);
        this.color = i;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, BreathParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, dustParticleOptions -> dustParticleOptions.color, ByteBufCodecs.FLOAT, ScalableParticleOptionsBase::getScale, BreathParticleOptions::new
    );

    @Override
    public ParticleType<BreathParticleOptions> getType() {
        return HearthHarvestClient.BREATH;
    }

    public Vector3f getColor() {
        return ARGB.vector3fFromRGB24(this.color);
    }
}
