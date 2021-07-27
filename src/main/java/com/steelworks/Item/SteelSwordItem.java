package com.steelworks.Item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SteelSwordItem extends SwordItem {
	public SteelSwordItem(IItemTier tier, int damage, float speed, Properties prop) {
		super(tier, damage, speed, prop);
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 30;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
		return stack;
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		player.getCooldowns().addCooldown(this, 70);
		ItemStack itemstack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return ActionResult.consume(itemstack);
	}
}
