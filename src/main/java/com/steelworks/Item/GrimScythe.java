package com.steelworks.Item;

import com.steelworks.CommonSetup;
import com.steelworks.Network.CustomParticleMessage;
import com.steelworks.Registry.ItemRegistry;
import com.steelworks.Steelworks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class GrimScythe extends SteelScythe {

	public GrimScythe(IItemTier tier, int damage, float speed, Properties prop) {
		super(tier, damage, speed, prop);
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.SPEAR;
	}

	public void releaseUsing(ItemStack stack, World world, LivingEntity living, int tick) {
		if (living instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity) living;
			//todo Grim Scythe charge up

		}
	}

	public static void handleLivingDeath(LivingDeathEvent e) {
		if (e.getSource().getEntity() != null && e.getSource().getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) e.getSource().getEntity();
			if (player.getMainHandItem().getItem() == ItemRegistry.GRIM_SCYTHE.get()) {
				double x = e.getEntity().getX();
				double y = e.getEntity().getY() + e.getEntity().getEyeHeight();
				double z = e.getEntity().getZ();
				double x2 = player.position().x;
				double y2 = player.position().y + player.getEyeHeight() - 0.25;
				double z2 = player.position().z;

				float lifesteal = player.getMaxHealth() * 0.10f;
				CommonSetup.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> player.level.dimension()), new CustomParticleMessage(new ResourceLocation(Steelworks.MODID, "lifesteal"), x, y, z, x2, y2, z2, (int) (lifesteal)));
				player.heal(lifesteal);
			}
		}
	}

	@Override
	public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
		return super.interactLivingEntity(stack, player, entity, hand);
	}
}
