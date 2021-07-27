package com.steelworks.Compat.JEI;

import com.steelworks.Block.ForgeFurnaceScreen;
import com.steelworks.Registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ForgeFurnaceRecipeCategory implements IRecipeCategory<ForgeFurnaceRecipeWrapper> {

	private final IDrawable background;
	private final IGuiHelper gui;

	public ForgeFurnaceRecipeCategory(IGuiHelper guihelper) {
		this.gui = guihelper;
		this.background = guihelper.createDrawable(ForgeFurnaceScreen.GUI, 52, 13, 72, 61);
	}

	@Override
	public ResourceLocation getUid() {
		return JEICompat.FORGE_FURNACE;
	}

	@Override
	public Class<? extends ForgeFurnaceRecipeWrapper> getRecipeClass() {
		return ForgeFurnaceRecipeWrapper.class;
	}

	@Override
	public String getTitle() {
		return BlockRegistry.FORGE_FURNACE.get().getName().getString();
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public IDrawable getIcon() {
		return gui.createDrawableIngredient(new ItemStack(BlockRegistry.FORGE_FURNACE.get()));
	}

	@Override
	public void setIngredients(ForgeFurnaceRecipeWrapper wrapper, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, wrapper.getInputs());
		ingredients.setOutput(VanillaTypes.ITEM, wrapper.getOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout layout, ForgeFurnaceRecipeWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup guistacks = layout.getItemStacks();

		guistacks.init(0, true, 3, 3);
		guistacks.init(1, true, 27, 3);
		guistacks.init(2, true, 51, 3);
		guistacks.init(3, false, 27, 40);

		for (int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size() && i < 3; i++) {
			guistacks.set(i, ingredients.getInputs(VanillaTypes.ITEM).get(i));
		}

		guistacks.set(3, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
	}

}
