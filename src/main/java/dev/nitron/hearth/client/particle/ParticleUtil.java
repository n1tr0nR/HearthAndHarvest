package dev.nitron.hearth.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DustParticle;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ParticleUtil {
    private static int ticksBreathing = 0;
    private static int ticksTillBreath = 100;

    public static void SpawnBreathParticle(Player player){
        if (ticksTillBreath > 0){
            ticksTillBreath--;
            if (ticksTillBreath == 0){
                ticksBreathing = player.getRandom().nextInt(15, 25);
            }
            return;
        }

        if (ticksBreathing > 0){
            ticksBreathing--;
            Level level = player.level();
            if (level instanceof ClientLevel clientLevel){
                BreathParticleOptions particle = new BreathParticleOptions(
                        0x999999, 1
                );

                Vec3 look = player.getLookAngle();
                Vec3 eyePos = player.getEyePosition(0).subtract(0, 0.2, 0);
                double px = eyePos.x + look.x * 0.3;
                double py = eyePos.y + look.y * 0.3;
                double pz = eyePos.z + look.z * 0.3;

                if (canSeeBreath(player)){
                    clientLevel.addParticle(particle,
                            px, py, pz,
                            0, -0.02F, 0);
                }
            }
            if (ticksBreathing == 0){
                ticksTillBreath = player.getRandom().nextInt(60, 110);
            }
        }
    }

    private static boolean canSeeBreath(Player player){
        boolean isHigh = player.getY() > 120;
        boolean isInCold = player.level().getBiome(player.getOnPos()).is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS);
        return isHigh || isInCold;
    }
}
