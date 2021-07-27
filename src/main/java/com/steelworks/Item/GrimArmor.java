package com.steelworks.Item;

import com.steelworks.Registry.EffectRegistry;
import com.steelworks.Registry.ItemRegistry;
import com.steelworks.Steelworks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class GrimArmor extends ArmorItem {
	public GrimArmor(EquipmentSlotType slot, Properties prop) {
		super(Grim, slot, prop);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (slot != EquipmentSlotType.LEGS && entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(EffectRegistry.RAGE.get())) {
			return Steelworks.MODID + ":textures/models/armor/" + Grim.getName() + "_layer_rage.png";
		}
		return Steelworks.MODID + ":textures/models/armor/" + Grim.getName() + "_layer_" + (slot != EquipmentSlotType.LEGS ? 1 : 2) + ".png";
	}

	public static final IArmorMaterial Grim = new IArmorMaterial() {
		private final int[] DURABILITY_PERSLOT = new int[] { 13, 15, 16, 11 };

		@Override
		public int getDurabilityForSlot(EquipmentSlotType slot) {
			return DURABILITY_PERSLOT[slot.getIndex()] * 35;
		}

		@Override
		public int getDefenseForSlot(EquipmentSlotType slot) {
			return new int[] { 3, 6, 8, 3 }[slot.getIndex()];
		}

		@Override
		public int getEnchantmentValue() {
			return 25;
		}

		@Override
		public SoundEvent getEquipSound() {
			return SoundEvents.ARMOR_EQUIP_NETHERITE;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.of(ItemRegistry.GRIMSTEEL_INGOT.get());
		}

		@Override
		public String getName() {
			return "grim";
		}

		@Override
		public float getToughness() {
			return 1f;
		}

		@Override
		public float getKnockbackResistance() {
			return 0.0f;
		}
	};
}
