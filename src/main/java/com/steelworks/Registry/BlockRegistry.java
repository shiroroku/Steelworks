package com.steelworks.Registry;

import com.steelworks.Block.ForgeFurnaceBlock;
import com.steelworks.Steelworks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BlockRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Steelworks.MODID);

	public static final RegistryObject<ForgeFurnaceBlock> FORGE_FURNACE = registerBlockAndItem("forge_furnace", ForgeFurnaceBlock::new);
	public static final RegistryObject<Block> STEEL_PLATE_BLOCK = registerBlockAndItem("steel_plate_block", () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(10.0f).harvestTool(ToolType.PICKAXE)));

	public static void init() {
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	/**
	 * Creates and returns a Block while also creating an Item for that block.
	 *
	 * @param id Block id
	 * @param supplier Block factory
	 * @return Registry object of supplied Block
	 */
	private static <I extends Block> RegistryObject<I> registerBlockAndItem(final String id, final Supplier<? extends I> supplier) {
		RegistryObject<I> createdBlock = BLOCKS.register(id, supplier);
		ItemRegistry.ITEMS.register(id, () -> new BlockItem(createdBlock.get(), new Item.Properties().tab(Steelworks.CREATIVETAB)));
		return createdBlock;
	}
}
