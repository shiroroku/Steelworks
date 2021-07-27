package com.steelworks.Item;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class SteelScythe extends HoeItem {

	private static final double sweepSize = 3.0D;

	public SteelScythe(IItemTier tier, int damage, float speed, Properties prop) {
		super(tier, damage, speed, prop);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment e) {
		if (e == Enchantments.SHARPNESS) {
			return true;
		}
		return super.canApplyAtEnchantingTable(stack, e);
	}

	public static void handlePlayerAttack(AttackEntityEvent e) {
		if (e.getPlayer().getMainHandItem().getItem() instanceof SteelScythe) {
			PlayerEntity player = e.getPlayer();
			float attackAttribute = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
			float strengthScale = player.getAttackStrengthScale(0.5F);
			player.resetAttackStrengthTicker();
			float damage = attackAttribute * strengthScale;

			for (LivingEntity target : player.level.getEntitiesOfClass(LivingEntity.class, e.getEntityLiving().getBoundingBox().inflate(sweepSize, 0.25D, sweepSize))) {
				if (target != player && !player.isAlliedTo(target)) {
					target.knockback(0.4F, MathHelper.sin(player.yRot * ((float) Math.PI / 180F)), -MathHelper.cos(player.yRot * ((float) Math.PI / 180F)));
					target.hurt(DamageSource.playerAttack(player), damage);
				}
			}
			if (strengthScale > 0.5F) {
				player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
				player.sweepAttack();
			}
			e.setCanceled(true);
		}
	}

	@Override
	public ActionResultType useOn(ItemUseContext ctx) {
		World world = ctx.getLevel();
		BlockState block = world.getBlockState(ctx.getClickedPos());
		if (!world.isClientSide && block.getBlock() instanceof CropsBlock && block.getValue(((CropsBlock) block.getBlock()).getAgeProperty()) >= ((CropsBlock) block.getBlock()).getMaxAge()) {
			world.destroyBlock(ctx.getClickedPos(), true);
			world.setBlock(ctx.getClickedPos(), block.getBlock().defaultBlockState(), 3);
			ctx.getItemInHand().hurtAndBreak(1, ctx.getPlayer(), (player) -> player.broadcastBreakEvent(ctx.getHand()));
		}
		return super.useOn(ctx);
	}
}
