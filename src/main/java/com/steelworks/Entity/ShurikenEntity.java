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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class ShurikenEntity extends ProjectileItemEntity {

	boolean isOnGround = false;

	double knockback = 1D;
	float damage = 3F;

	private static final DataParameter<Boolean> IS_HORIZONTAL = EntityDataManager.defineId(ShurikenEntity.class, DataSerializers.BOOLEAN);

	int onGroundTime = 0;
	float yRotOCapture;
	float yRotCapture;
	float xRotOCapture;
	float xRotCapture;
	BlockPos collidedBlock;

	public ShurikenEntity(EntityType<? extends ShurikenEntity> entityEntityType, World world) {
		super(entityEntityType, world);
		this.entityData.define(IS_HORIZONTAL, false);
	}

	public ShurikenEntity(World world, LivingEntity player) {
		super(EntityRegistry.SHURIKEN.get(), player, world);
		this.entityData.define(IS_HORIZONTAL, false);
	}

	public ShurikenEntity(World world, double x, double y, double z) {
		super(EntityRegistry.SHURIKEN.get(), x, y, z, world);
		this.entityData.define(IS_HORIZONTAL, false);
	}

	public void setHorizontal(boolean val) {
		this.entityData.set(IS_HORIZONTAL, val);
	}

	public boolean isHorizontal() {
		return this.entityData.get(IS_HORIZONTAL);
	}

	@Override
	public float getGravity() {
		return 0.02F;
	}

	@Override
	public Item getDefaultItem() {
		return ItemRegistry.SHURIKEN.get();
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

	@Override
	public void onHitEntity(EntityRayTraceResult result) {
		super.onHitEntity(result);
		result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), damage);
		this.remove();
		if (this.isHorizontal()) {
			Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(knockback * 0.6D);
			if (vector3d.lengthSqr() > 0.0D) {
				result.getEntity().push(vector3d.x, 0.1D, vector3d.z);
			}

		}
	}

	@Override
	public void playerTouch(PlayerEntity player) {
		if (!this.level.isClientSide && this.isOnGround) {
			this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(ItemRegistry.SHURIKEN.get())));
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
		//if (!this.level.isClientSide) {
			//this.level.broadcastEntityEvent(this, (byte) 3);
		//}
		this.collidedBlock = result.getBlockPos();
		this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOD_BREAK, SoundCategory.NEUTRAL, 0.25F, random.nextFloat() * 0.5F + 2F);
	}

}
