package com.steelworks.Registry;

import com.steelworks.Entity.*;
import com.steelworks.Steelworks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class EntityRegistry {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Steelworks.MODID);

	public static final RegistryObject<EntityType<SenbonEntity>> SENBON = ENTITIES.register("senbon", () -> EntityType.Builder.<SenbonEntity>of(SenbonEntity::new, EntityClassification.MISC).sized(0.8f, 0.8f).build("senbon"));
	public static final RegistryObject<EntityType<ShurikenEntity>> SHURIKEN = ENTITIES.register("shuriken", () -> EntityType.Builder.<ShurikenEntity>of(ShurikenEntity::new, EntityClassification.MISC).sized(0.8f, 0.8f).build("shuriken"));

	public static final String ID_GRIM_KNIGHT = "grim_knight";
	private static final EntityType<GrimKnightEntity> GRIM_KNIGHT_BUILDER = EntityType.Builder.of(GrimKnightEntity::new, EntityClassification.MONSTER).build(ID_GRIM_KNIGHT);
	public static final RegistryObject<EntityType<GrimKnightEntity>> GRIM_KNIGHT = ENTITIES.register(ID_GRIM_KNIGHT, () -> GRIM_KNIGHT_BUILDER);
	public static final RegistryObject<Item> EGG_CTHULHU_CULTIST = ItemRegistry.ITEMS.register(ID_GRIM_KNIGHT + "_egg", () -> createSpawnEgg(GRIM_KNIGHT_BUILDER, new Color(64, 56, 66), new Color(93, 227, 239)));

	public static void init() {
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityRegistry::setupAttributes);
	}

	public static void initClient() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SENBON.get(), SenbonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SHURIKEN.get(), ShurikenRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GRIM_KNIGHT.get(), GrimKnightRenderer::new);
	}

	public static void setupAttributes(EntityAttributeCreationEvent event) {
		event.put(EntityRegistry.GRIM_KNIGHT.get(), GrimKnightEntity.createAttributes().build());
	}

	private static SpawnEggItem createSpawnEgg(EntityType<?> entity, Color color1, Color color2) {
		return new SpawnEggItem(entity, color1.getRGB(), color2.getRGB(), (new Item.Properties()).tab(Steelworks.CREATIVETAB));
	}

}
