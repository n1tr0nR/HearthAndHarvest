package dev.nitron.hearth.client;

import com.mojang.serialization.MapCodec;
import dev.nitron.hearth.HearthHarvest;
import dev.nitron.hearth.client.particle.BreathParticle;
import dev.nitron.hearth.client.particle.BreathParticleOptions;
import dev.nitron.hearth.client.particle.ParticleUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.DustParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class HearthHarvestClient implements ClientModInitializer {
	public static final ParticleType<BreathParticleOptions> BREATH = register(
			"breath", false, particleType -> BreathParticleOptions.CODEC, particleType -> BreathParticleOptions.STREAM_CODEC
	);

	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(BREATH, BreathParticle.Provider::new);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			LocalPlayer player = client.player;
			if (player != null){
				ParticleUtil.SpawnBreathParticle(player);
			}
		});
	}

	private static <T extends ParticleOptions> ParticleType<T> register(
			String string,
			boolean bl,
			Function<ParticleType<T>, MapCodec<T>> function,
			Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> function2
	) {
		return Registry.register(BuiltInRegistries.PARTICLE_TYPE, ResourceLocation.fromNamespaceAndPath(HearthHarvest.MOD_ID, string), new ParticleType<T>(bl) {
			@Override
			public MapCodec<T> codec() {
				return (MapCodec<T>)function.apply(this);
			}

			@Override
			public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
				return (StreamCodec<? super RegistryFriendlyByteBuf, T>)function2.apply(this);
			}
		});
	}
}