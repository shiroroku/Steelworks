package com.steelworks.Item;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraftforge.common.ToolType;

public class SteelPick extends ShovelItem {
	public SteelPick(IItemTier tiered, int damage, float speed, Properties prop) {
		super(tiered, damage, speed, prop);
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		if (state.getHarvestTool() == ToolType.PICKAXE || state.getHarvestTool() == ToolType.SHOVEL) {
			return this.getTier().getLevel() >= state.getHarvestLevel();
		}
		Material mat = state.getMaterial();
		return mat == Material.STONE || mat == Material.METAL || mat == Material.HEAVY_METAL || mat == Material.SNOW || mat == Material.TOP_SNOW;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material mat = state.getMaterial();
		return mat != Material.METAL && mat != Material.HEAVY_METAL && mat != Material.STONE ? super.getDestroySpeed(stack, state) : this.speed;
	}
}
