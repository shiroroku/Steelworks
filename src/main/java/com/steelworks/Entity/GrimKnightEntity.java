package com.steelworks.Entity;

import com.steelworks.Registry.ItemRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GrimKnightEntity extends MonsterEntity {

	public static final double HEALTH = 40D;
	public static final double SPEED = 0.3D;
	public static final double ATTACKSPEED = 1.5D;
	public static final double ARMOR = 10D;
	public static final double DAMAGE = 4.0D;
	public static final double RANGE = 16.0D;
	public static final double KNOCKBACK_RES = 1.0D;
	public static final int XP = 20;

	public GrimKnightEntity(EntityType<? extends MonsterEntity> entity, World world) {
		super(entity, world);
		this.xpReward = XP;
		this.setDropChance(EquipmentSlotType.MAINHAND, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(GrimKnightEntity.class));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes().add(Attributes.KNOCKBACK_RESISTANCE, KNOCKBACK_RES).add(Attributes.ARMOR, ARMOR).add(Attributes.ATTACK_SPEED, ATTACKSPEED).add(Attributes.MAX_HEALTH, HEALTH).add(Attributes.MOVEMENT_SPEED, SPEED).add(Attributes.ATTACK_DAMAGE, DAMAGE).add(Attributes.FOLLOW_RANGE, RANGE);
	}

	@Override
	@Nullable
	public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
		entityData = super.finalizeSpawn(serverWorld, difficulty, spawnReason, entityData, nbt);
		this.populateDefaultEquipmentSlots(difficulty);
		return entityData;
	}

	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
		this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemRegistry.GRIM_CLEAVER.get()));
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntitySize size) {
		return 1.74F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.IRON_GOLEM_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLAZE_DEATH;
	}
}
