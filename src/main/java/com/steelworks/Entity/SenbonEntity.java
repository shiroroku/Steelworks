package com.steelworks.Entity;

import com.steelworks.Registry.EntityRegistry;
import com.steelworks.Registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class SenbonEntity extends ProjectileItemEntity {

	public SenbonEntity(EntityType<? extends SenbonEntity> entityEntityType, World world) {
		super(entityEntityType, world);
	}

	public SenbonEntity(World world, LivingEntity player) {
		super(EntityRegistry.SENBON.get(), player, world);
	}

	public SenbonEntity(World world, double x, double y, double z) {
		super(EntityRegistry.SENBON.get(), x, y, z, world);
	}

	@Override
	public Item getDefaultItem() {
		return ItemRegistry.SENBON.get();
	}

	@Nonnull
	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@OnlyIn(Dist.CLIENT)
	private IParticleData getParticle() {
		return new ItemParticleData(ParticleTypes.ITEM, this.getItem());
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte event) {
		if (event == 3) {
			float speed = 0.5f;
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(this.getParticle(), this.getX(), this.getY(), this.getZ(), (-0.5D + this.random.nextDouble()) * speed, (-0.5D + this.random.nextDouble()) * speed, (-0.5D + this.random.nextDouble()) * speed);
			}
		}

	}

	@Override
	public void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 3.0f);
	}

	@Override
	public void onHit(RayTraceResult result) {
		super.onHit(result);
		if (result.getType() == RayTraceResult.Type.BLOCK) {
			if (!this.level.isClientSide) {
				this.level.broadcastEntityEvent(this, (byte) 3);
				this.remove();
			}
			this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ITEM_BREAK, SoundCategory.NEUTRAL, 0.25F, random.nextFloat() * 0.5F + 3F);
		}
	}
}
