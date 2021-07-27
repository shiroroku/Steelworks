package com.steelworks.Registry;

import com.steelworks.Steelworks;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Steelworks.MODID);

	public static final RegistryObject<BasicParticleType> LIFESTEAL = PARTICLES.register("lifesteal", () -> new BasicParticleType(true));

	public static void init() {
		PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
