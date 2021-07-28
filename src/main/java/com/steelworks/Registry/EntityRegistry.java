package com.steelworks.Registry;

import com.steelworks.Entity.SenbonEntity;
import com.steelworks.Steelworks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Steelworks.MODID);

	public static final RegistryObject<EntityType<SenbonEntity>> SENBON = ENTITIES.register("senbon", () -> EntityType.Builder.<SenbonEntity>of(SenbonEntity::new, EntityClassification.MISC).build("senbon"));

	public static void init() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}