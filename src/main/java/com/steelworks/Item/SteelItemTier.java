package com.steelworks.Item;

import com.steelworks.Registry.ItemRegistry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public class SteelItemTier implements IItemTier {
	@Override
	public int getUses() {
		return 500;
	}

	@Override
	public float getSpeed() {
		return 6f;
	}

	@Override
	public float getAttackDamageBonus() {
		return 2.5f;
	}

	@Override
	public int getLevel() {
		return 2;
	}

	@Override
	public int getEnchantmentValue() {
		return 17;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.of(ItemRegistry.STEEL_INGOT.get());
	}
}
