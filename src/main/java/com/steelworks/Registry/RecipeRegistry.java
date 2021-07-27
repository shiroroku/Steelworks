package com.steelworks.Registry;

import com.steelworks.Recipe.ForgeFurnaceRecipe;
import com.steelworks.Recipe.ForgeFurnaceRecipeType;
import com.steelworks.Recipe.HammerRepairRecipe;
import com.steelworks.Recipe.HammerRepairType;
import com.steelworks.Steelworks;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistry {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Steelworks.MODID);

	public final static RegistryObject<IRecipeSerializer<HammerRepairRecipe>> HAMMER_REPAIR_SERIALIZER = SERIALIZERS.register("hammer_repair", () -> new SpecialRecipeSerializer<>(HammerRepairRecipe::new));
	public static final IRecipeType<HammerRepairRecipe> HAMMER_REPAIR_TYPE = new HammerRepairType();

	public final static RegistryObject<IRecipeSerializer<ForgeFurnaceRecipe>> FORGE_FURNACE_SERIALIZER = SERIALIZERS.register("forge_furnace_recipe", ForgeFurnaceRecipe.Serializer::new);
	public static final IRecipeType<ForgeFurnaceRecipe> FORGE_FURNACE_TYPE = new ForgeFurnaceRecipeType();

	public static void init() {
		SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

		Registry.register(Registry.RECIPE_TYPE, HAMMER_REPAIR_TYPE.toString(), HAMMER_REPAIR_TYPE);
		Registry.register(Registry.RECIPE_TYPE, FORGE_FURNACE_TYPE.toString(), FORGE_FURNACE_TYPE);
	}
}
