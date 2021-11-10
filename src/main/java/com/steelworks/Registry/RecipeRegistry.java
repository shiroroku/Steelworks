package com.steelworks.Registry;

import com.steelworks.Recipe.ForgeFurnaceRecipe;
import com.steelworks.Recipe.HammerRepairRecipe;
import com.steelworks.Steelworks;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Steelworks.MODID);

	public static final RegistryObject<IRecipeSerializer<HammerRepairRecipe>> HAMMER_REPAIR_SERIALIZER = SERIALIZERS.register("hammer_repair", () -> new SpecialRecipeSerializer<>(HammerRepairRecipe::new));

	public static final RegistryObject<IRecipeSerializer<ForgeFurnaceRecipe>> FORGE_FURNACE_SERIALIZER = SERIALIZERS.register("forge_furnace_recipe", () -> ForgeFurnaceRecipe.SERIALIZER);

	public static void init() {
		SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
