package com.steelworks.Item;

import com.steelworks.Entity.SenbonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SenbonItem extends Item {
	public SenbonItem(Properties prop) {
		super(prop);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundCategory.NEUTRAL, 0.5F, random.nextFloat() * 0.25F + 1F);
		if (!world.isClientSide) {
			SenbonEntity senbon = new SenbonEntity(world, player);
			senbon.setItem(itemstack);
			senbon.shootFromRotation(player, player.xRot, player.yRot, 0F, 1.2F, 1.2F);
			world.addFreshEntity(senbon);
		}

		if (!player.isCreative()) {
			itemstack.shrink(1);
		}

		player.getCooldowns().addCooldown(this, 10);
		return ActionResult.sidedSuccess(itemstack, world.isClientSide());
	}
}
