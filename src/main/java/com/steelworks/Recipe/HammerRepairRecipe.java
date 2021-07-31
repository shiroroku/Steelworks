package com.steelworks.Recipe;

import com.steelworks.Registry.ItemRegistry;
import com.steelworks.Registry.RecipeRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HammerRepairRecipe extends SpecialRecipe {

	//public static final IRecipeSerializer<?> SERIALIZER = new SpecialRecipeSerializer<>(HammerRepairRecipe::new).setRegistryName(Steelworks.MODID, "hammer_repair");

	//public static final IRecipeType<HammerRepairRecipe> TYPE = IRecipeType.register("hammer_repair");

	public HammerRepairRecipe(ResourceLocation recipe) {
		super(recipe);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		List<ItemStack> items = new ArrayList<>();
		for (int s = 0; s < inv.getContainerSize(); s++) {
			items.add(inv.getItem(s));
		}

		items.removeIf(ItemStack::isEmpty);

		if (items.size() != 2) {
			return false;
		}

		boolean flag_hammer = false;
		boolean flag_repairable = false;

		for (ItemStack item : items) {
			if (item.getItem() == ItemRegistry.STEEL_HAMMER.get()) {
				flag_hammer = true;
			} else {
				if (item.isRepairable()) {
					if (item.getDamageValue() != 0) {
						flag_repairable = true;
					}
				}
			}
		}

		return flag_hammer && flag_repairable;
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		List<ItemStack> items = new ArrayList<>();
		for (int s = 0; s < inv.getContainerSize(); s++) {
			items.add(inv.getItem(s));
		}

		items.removeIf(ItemStack::isEmpty);

		ItemStack result = ItemStack.EMPTY;
		for (ItemStack item : items) {
			if (item.isRepairable()) {
				if (item.getDamageValue() != 0) {
					result = item.copy();
					result.setDamageValue(Math.max(item.getDamageValue() - 20, 0));
					break;
				}
			}
		}

		return result;
	}

	@Override
	public boolean canCraftInDimensions(int w, int h) {
		return w * h >= 2;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;//RecipeRegistry.HAMMER_REPAIR_SERIALIZER.get();
	}
/*
	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}*/

}
