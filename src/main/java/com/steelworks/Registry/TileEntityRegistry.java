package com.steelworks.Registry;

import com.steelworks.Steelworks;
import com.steelworks.Block.ForgeFurnaceTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityRegistry {

	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Steelworks.MODID);

	public static final RegistryObject<TileEntityType<ForgeFurnaceTileEntity>> FORGE_FURNACE = TILE_ENTITIES.register("forge_furnace", () -> TileEntityType.Builder.of(ForgeFurnaceTileEntity::new, BlockRegistry.FORGE_FURNACE.get()).build(null));

	public static void init() {
		TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
