package com.steelworks.Entity;

import com.steelworks.Registry.EntityRegistry;
import com.steelworks.Registry.ItemRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class SenbonEntity extends ProjectileItemEntity {

	boolean isOnGround = false;
	int onGroundTime = 0;

	float yRotOCapture;
	float yRotCapture;
	float xRotOCapture;
	float xRotCapture;
	BlockPos collidedBlock;

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
	public float getGravity() {
		return 0.02F;
	}

	@Override
	public Item getDefaultItem() {
		return ItemRegistry.SENBON.get();
	}

	@Override
	public void tick() {
		super.tick();
		if (isOnGround) {
			onGroundTime++;
			if (onGroundTime >= 20 * 60 * 30) {
				this.remove();
			}

			if (this.level.getBlockState(collidedBlock).isAir()) {
				isOnGround = false;
				this.setNoGravity(false);
				this.setDeltaMovement(0, this.getGravity(), 0);
			}
		} else {
			//So we dont keep rotating towards velocity when on the ground
			this.yRotOCapture = yRotO;
			this.yRotCapture = yRot;
			this.xRotOCapture = xRotO;
			this.xRotCapture = xRot;
		}
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
	public void playerTouch(PlayerEntity player) {
		if (!this.level.isClientSide && this.isOnGround) {
			this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(ItemRegistry.SENBON.get())));
			this.remove();
		}
	}

	@Override
	public void onHitBlock(BlockRayTraceResult result) {
		isOnGround = true;
		float distance = 0.5f;
		this.setPosAndOldPos(MathHelper.lerp(distance, result.getLocation().x, this.getX()), MathHelper.lerp(distance, result.getLocation().y, this.getY()), MathHelper.lerp(distance, result.getLocation().z, this.getZ()));
		this.setDeltaMovement(0, 0, 0);
		this.setNoGravity(true);
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
		}
		this.collidedBlock = result.getBlockPos();
		this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOD_BREAK, SoundCategory.NEUTRAL, 0.25F, random.nextFloat() * 0.5F + 2F);
	}

}
