package com.steelworks.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ForgeFurnaceRecipe implements IRecipe<IInventory> {

	public static final IRecipeType<ForgeFurnaceRecipe> TYPE = IRecipeType.register("forge_furnace_recipe");
	public static final Serializer SERIALIZER = new Serializer();

	private final ResourceLocation ID;
	private final NonNullList<Ingredient> INPUTS;
	private final ItemStack OUTPUT;
	private final float XP;
	private final int CRAFTTIME;
	private final boolean ISSIMPLE;

	public ForgeFurnaceRecipe(ResourceLocation id, NonNullList<Ingredient> inputStacks, ItemStack outputStack, float xp, int craftTime) {
		this.ID = id;
		this.INPUTS = inputStacks;
		this.OUTPUT = outputStack;
		this.XP = xp;
		this.CRAFTTIME = craftTime;
		this.ISSIMPLE = inputStacks.stream().allMatch(Ingredient::isSimple);
	}

	/**
	 * Not used, but required by IRecipe. ForgeFurnaceTileEntity only uses matches(IItemHandler inv).
	 */
	@Override
	public boolean matches(IInventory inv, World world) {
		RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < 3; ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				if (ISSIMPLE) {
					recipeitemhelper.accountStack(itemstack, 1);
				} else {
					inputs.add(itemstack);
				}
			}
		}

		return i == this.INPUTS.size() && (ISSIMPLE ? recipeitemhelper.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.INPUTS) != null);
	}

	public boolean matches(IItemHandler inv) {
		RecipeItemHelper recipeHelper = new RecipeItemHelper();
		java.util.List<ItemStack> inputStacks = new java.util.ArrayList<>();
		int count = 0;
		for (int i = 0; i < 3; ++i) {
			ItemStack itemstack = inv.getStackInSlot(i);
			if (!itemstack.isEmpty()) {
				++count;
				if (ISSIMPLE) {
					recipeHelper.accountStack(itemstack, 1);
				} else {
					inputStacks.add(itemstack);
				}
			}
		}
		return count == this.INPUTS.size() && (ISSIMPLE ? recipeHelper.canCraft(this, null) : RecipeMatcher.findMatches(inputStacks, this.INPUTS) != null);
	}

	public ItemStack getOutput() {
		return this.OUTPUT.copy();
	}

	public float getXP() {
		return this.XP;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.INPUTS;
	}

	@Override
	public boolean isSpecial() {
		return this.ISSIMPLE;
	}

	@Override
	public ItemStack assemble(IInventory inv) {
		return this.OUTPUT.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return this.OUTPUT;
	}

	@Override
	public ResourceLocation getId() {
		return this.ID;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return TYPE;
	}

	public int getCraftTime() {
		return this.CRAFTTIME;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ForgeFurnaceRecipe> {

		@Override
		public ForgeFurnaceRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			try {
				NonNullList<Ingredient> inputStacks = NonNullList.create();
				for (int i = 0; i < JSONUtils.getAsJsonArray(json, "ingredients").size(); ++i) {
					Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonArray(json, "ingredients").get(i));
					if (!ingredient.isEmpty()) {
						inputStacks.add(ingredient);
					}
				}

				if (inputStacks.isEmpty()) {
					throw new JsonParseException("No ingredients for forge furnace recipe.");
				} else {
					if (inputStacks.size() > 3) {
						throw new JsonParseException("Too many ingredients for forge furnace recipe, the max is 3.");
					} else {
						ItemStack outputStack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));
						float xp = JSONUtils.getAsFloat(json, "xp");
						int craftTime = JSONUtils.getAsInt(json, "craft_time");
						return new ForgeFurnaceRecipe(recipeId, inputStacks, outputStack, xp, craftTime);
					}
				}
			} catch (JsonSyntaxException e) {
				return null;
			}
		}

		@Override
		public ForgeFurnaceRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			int inputSize = buffer.readVarInt();
			NonNullList<Ingredient> inputStacks = NonNullList.withSize(inputSize, Ingredient.EMPTY);
			for (int i = 0; i < inputStacks.size(); ++i) {
				inputStacks.set(i, Ingredient.fromNetwork(buffer));
			}
			float xp = buffer.readFloat();
			int craftTime = buffer.readInt();
			ItemStack outputStack = buffer.readItem();
			return new ForgeFurnaceRecipe(recipeId, inputStacks, outputStack, xp, craftTime);
		}

		@Override
		public void toNetwork(PacketBuffer buffer, ForgeFurnaceRecipe recipe) {
			buffer.writeVarInt(recipe.INPUTS.size());
			for (Ingredient ingredient : recipe.INPUTS) {
				ingredient.toNetwork(buffer);
			}
			buffer.writeFloat(recipe.XP);
			buffer.writeInt(recipe.CRAFTTIME);
			buffer.writeItem(recipe.OUTPUT);
		}
	}
}
