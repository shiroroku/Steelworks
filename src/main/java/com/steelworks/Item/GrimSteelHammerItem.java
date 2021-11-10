package com.steelworks.Item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class GrimSteelHammerItem extends Item {
	private static final Random rand = new Random();

	public GrimSteelHammerItem(Properties prop) {
		super(prop);
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack) {
		ItemStack stack = itemStack.copy();
		if (!hasContainerItem(itemStack) || stack.hurt(1, rand, null)) {
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
}
