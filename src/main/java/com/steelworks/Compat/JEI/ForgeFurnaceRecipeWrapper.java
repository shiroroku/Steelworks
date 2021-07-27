package com.steelworks.Compat.JEI;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ForgeFurnaceRecipeWrapper {
	private final List<List<ItemStack>>  INPUTS;
	private final ItemStack OUTPUT;

	public ForgeFurnaceRecipeWrapper(List<List<ItemStack>> input, ItemStack output) {
		INPUTS = input;
		OUTPUT = output;
	}

	public List<List<ItemStack>> getInputs() {
		return INPUTS;
	}

	public ItemStack getOutput() {
		return OUTPUT;
	}
}
