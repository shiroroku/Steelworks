package com.steelworks.Item;

import com.steelworks.Entity.ShurikenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ShurikenItem extends Item {
	public ShurikenItem(Properties prop) {
		super(prop);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundCategory.NEUTRAL, 0.5F, random.nextFloat() * 0.25F + 1F);
		if (!world.isClientSide) {
			if (player.isCrouching() && itemstack.getCount() >= 3) {

				float spread = 20f;
				for (int i = -1; i < 2; i++) {
					ShurikenEntity shuriken = new ShurikenEntity(world, player);
					shuriken.setHorizontal(true);
					shuriken.setItem(itemstack);
					shuriken.shootFromRotation(player, player.xRot, player.yRot + (spread * i), 0F, 1F, 1F);
					world.addFreshEntity(shuriken);
				}

				if (!player.isCreative()) {
					itemstack.shrink(2);
				}
			} else {

				ShurikenEntity shuriken = new ShurikenEntity(world, player);
				shuriken.setItem(itemstack);
				shuriken.shootFromRotation(player, player.xRot, player.yRot, 0F, 1.8F, 1.8F);
				world.addFreshEntity(shuriken);
			}
		}

		if (!player.isCreative()) {
			itemstack.shrink(1);
		}

		player.getCooldowns().addCooldown(this, 10);
		return ActionResult.sidedSuccess(itemstack, world.isClientSide());
	}
}
