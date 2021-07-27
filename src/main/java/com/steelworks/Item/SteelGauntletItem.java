package com.steelworks.Item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class SteelGauntletItem extends TieredItem implements IVanishable {
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;
	private UUID value = UUID.nameUUIDFromBytes("Gauntlet Armor".getBytes());

	public SteelGauntletItem(IItemTier tier, int damage, float speed, Properties prop) {
		super(tier, prop);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ARMOR, new AttributeModifier(value, "Armor modifier", 2D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", (float) damage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed, AttributeModifier.Operation.ADDITION));
		defaultModifiers = builder.build();
	}

	@Override
	@SuppressWarnings("deprecation")
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
		return slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		return hand != Hand.MAIN_HAND ? ActionResult.success(player.getItemInHand(hand)) : super.use(world, player, hand);
	}

	@Override
	public float getDestroySpeed(ItemStack item, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !state.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack item, LivingEntity living, LivingEntity player) {
		item.hurtAndBreak(1, player, (p) -> {
			p.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack item, World world, BlockState state, BlockPos pos, LivingEntity entity) {
		if (state.getDestroySpeed(world, pos) != 0.0F) {
			item.hurtAndBreak(2, entity, (player) -> {
				player.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
			});
		}
		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return state.is(Blocks.COBWEB);
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (hand != Hand.MAIN_HAND) {
			stack.hurtEnemy(target, player);
			player.attack(target);
			return ActionResultType.SUCCESS;
		}
		return super.interactLivingEntity(stack, player, target, hand);
	}
}
