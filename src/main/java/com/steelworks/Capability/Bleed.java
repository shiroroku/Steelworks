package com.steelworks.Capability;

import com.steelworks.CommonSetup;
import com.steelworks.Data.DataConfigJsonReloader;
import com.steelworks.Network.BleedUpdateMessage;
import com.steelworks.Steelworks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class Bleed {

	private int stacks;
	private static final float decayInterval = 4;
	private static final int maxStacks = 10;
	private static final float bleedDamage = 0.2f;

	public Bleed() {
		this(0);
	}

	public Bleed(int stacks) {
		this.stacks = stacks;
	}

	public static Bleed create() {
		return new Bleed();
	}

	public static void handleLivingUpdate(LivingEvent.LivingUpdateEvent e) {

		if (e.getEntity().tickCount % (20 * decayInterval) == 0) {
			Bleed target_bleed = e.getEntityLiving().getCapability(BleedCapability.CAPABILITY).orElse(null);
			if (target_bleed != null) {
				if (target_bleed.getStacks() > 0) {
					target_bleed.setStacks(target_bleed.getStacks() - 1, e.getEntityLiving());
				}
			}
		}
	}

	public static void handleLivingDamage(LivingDamageEvent e) {
		if (e.getSource().getEntity() != null) {
			if (e.getSource().getEntity() instanceof LivingEntity) {

				LivingEntity attacker = (LivingEntity) e.getSource().getEntity();
				ItemStack heldItem = attacker.getMainHandItem();
				int bleedDamage = create().getBleedValueFromItem(heldItem);
				if (bleedDamage > 0) {
					Bleed target_bleed = e.getEntityLiving().getCapability(BleedCapability.CAPABILITY).orElse(null);

					if (target_bleed == null) {
						Steelworks.LOGGER.error("Bleed capability missing from entity!");
					} else {
						target_bleed.setStacks(bleedDamage + target_bleed.getStacks(), e.getEntityLiving());
					}
				}
			}
		}
	}

	public void setStacks(int stacks, LivingEntity entity) {
		if (entity != null) {
			if (stacks >= maxStacks) {
				stacks = 0;
				entity.hurt(DamageSource.GENERIC, entity.getMaxHealth() * bleedDamage);
			}
		}

		this.stacks = stacks;
		if (entity instanceof ServerPlayerEntity) {
			CommonSetup.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entity), new BleedUpdateMessage(stacks));
		}
	}

	public int getBleedValueFromItem(ItemStack item) {
		//From nbttag
		if (item.hasTag() && item.getTag().contains("bleed_damage") && item.getTag().getInt("bleed_damage") > 0) {
			return item.getTag().getInt("bleed_damage");
		}

		//From data
		return DataConfigJsonReloader.getBleedAmountFromItem(item.getItem().getRegistryName());
	}

	public int getStacks() {
		return stacks;
	}

	public static class NBTStorage implements Capability.IStorage<Bleed> {

		@Override
		public INBT writeNBT(Capability<Bleed> capability, Bleed instance, Direction side) {
			return IntNBT.valueOf(instance.stacks);
		}

		@Override
		public void readNBT(Capability<Bleed> capability, Bleed instance, Direction side, INBT nbt) {
			int stacks = 0;
			if (nbt.getType() == IntNBT.TYPE) {
				stacks = ((IntNBT) nbt).getAsInt();
			}
			instance.setStacks(stacks, null);
		}
	}

}
