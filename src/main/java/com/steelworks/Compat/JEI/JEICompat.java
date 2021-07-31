package com.steelworks.Compat.JEI;

import com.steelworks.Block.ForgeFurnaceScreen;
import com.steelworks.Recipe.ForgeFurnaceRecipe;
import com.steelworks.Registry.BlockRegistry;
import com.steelworks.Registry.RecipeRegistry;
import com.steelworks.Steelworks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JEICompat implements IModPlugin {

	public static final ResourceLocation FORGE_FURNACE = new ResourceLocation(Steelworks.MODID, "forge_furnace");

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Steelworks.MODID, "recipes");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new ForgeFurnaceRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		registry.addRecipes(convertCrucibleRecipes(), FORGE_FURNACE);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.FORGE_FURNACE.get()), FORGE_FURNACE);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		registry.addRecipeClickArea(ForgeFurnaceScreen.class, 83, 36, 10, 15, FORGE_FURNACE);
	}

	private List<ForgeFurnaceRecipeWrapper> convertCrucibleRecipes() {
		List<ForgeFurnaceRecipeWrapper> recipesconverted = new ArrayList<>();
		for (final ForgeFurnaceRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ForgeFurnaceRecipe.TYPE)) {
			List<List<ItemStack>> stacks = new ArrayList<>();
			for (Ingredient ing : recipe.getIngredients()) {
				stacks.add(Arrays.asList(ing.getItems()));
			}
			recipesconverted.add(new ForgeFurnaceRecipeWrapper(stacks, recipe.getOutput()));
		}
		return recipesconverted;
	}

}
