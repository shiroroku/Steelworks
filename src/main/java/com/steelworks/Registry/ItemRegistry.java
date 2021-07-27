package com.steelworks.Registry;

import com.steelworks.Item.*;
import com.steelworks.Steelworks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Steelworks.MODID);

	public static RegistryObject<Item> CRUSHED_COAL = ITEMS.register("crushed_coal", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)) {
		@Override
		public int getBurnTime(ItemStack itemStack) {
			return 800;
		}
	});
	public static RegistryObject<Item> CRUSHED_CHARCOAL = ITEMS.register("crushed_charcoal", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)) {
		@Override
		public int getBurnTime(ItemStack itemStack) {
			return 800;
		}
	});
	public static RegistryObject<Item> CRUDE_STEEL = ITEMS.register("crude_steel", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIMSTEEL_INGOT = ITEMS.register("grimsteel_ingot", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIM_BOOTS = ITEMS.register("grim_boots", () -> new GrimArmor(EquipmentSlotType.FEET, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIM_CHESTPLATE = ITEMS.register("grim_chestplate", () -> new GrimArmor(EquipmentSlotType.CHEST, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIM_HELMET = ITEMS.register("grim_helmet", () -> new GrimArmor(EquipmentSlotType.HEAD, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIM_LEGGINGS = ITEMS.register("grim_leggings", () -> new GrimArmor(EquipmentSlotType.LEGS, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> GRIM_SCYTHE = ITEMS.register("grim_scythe", () -> new GrimScythe(new SteelItemTier(), 5, -2.8f, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_GAUNTLET = ITEMS.register("steel_gauntlet", () -> new SteelGauntletItem(new SteelItemTier(), 1, 0.5F, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_HAMMER = ITEMS.register("steel_hammer", () -> new SteelHammerItem(new Item.Properties().tab(Steelworks.CREATIVETAB).durability(64).setNoRepair()));
	public static RegistryObject<Item> STEEL_INGOT = ITEMS.register("steel_ingot", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_KNIFE = ITEMS.register("steel_knife", () -> new SwordItem(new SteelItemTier(), 1, -1.8F, new Item.Properties().tab(ModList.get().isLoaded("farmersdelight") ? Steelworks.CREATIVETAB : null)));
	public static RegistryObject<Item> STEEL_NUGGET = ITEMS.register("steel_nugget", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_PICK = ITEMS.register("steel_pick", () -> new SteelPick(new SteelItemTier(), 2, -3.0F, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_PLATE = ITEMS.register("steel_plate", () -> new Item(new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_SCYTHE = ITEMS.register("steel_scythe", () -> new SteelScythe(new SteelItemTier(), 4, -2.8F, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_SWORD = ITEMS.register("steel_sword", () -> new SteelSwordItem(new SteelItemTier(), 5, -3.0F, new Item.Properties().tab(Steelworks.CREATIVETAB)));
	public static RegistryObject<Item> STEEL_WRENCH = ITEMS.register("steel_wrench", () -> new SteelWrench(new SteelItemTier(), new Item.Properties().tab(Steelworks.CREATIVETAB)));

	public static void init() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
