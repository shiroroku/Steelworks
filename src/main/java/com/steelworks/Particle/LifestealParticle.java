package com.steelworks.Particle;

import com.steelworks.Registry.ItemRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class LifestealParticle extends SpriteTexturedParticle {

	private static final Random r = new Random();
	private final Vector3d startingPos;
	private Vector3d endingPos;
	private final Vector3d controlPoint;
	private final Vector3d controlPoint2;

	public LifestealParticle(ClientWorld world, double x, double y, double z, double xEnd, double yEnd, double zEnd) {
		super(world, x, y, z, 0, 0, 0);
		startingPos = new Vector3d(x, y, z);
		endingPos = new Vector3d(xEnd, yEnd, zEnd);
		double rad = Math.toRadians(360 * r.nextDouble());
		controlPoint = new Vector3d(startingPos.x + Math.sin(rad) * 2, startingPos.y + 1.5, Math.cos(rad) * 2 + startingPos.z);
		controlPoint2 = new Vector3d(endingPos.x, endingPos.y + 1, endingPos.z);
		this.scale(0.8f + 0.5f * r.nextFloat());
		this.setLifetime(18);
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		float t = (float) this.age / this.lifetime;

		double lerpA1X = MathHelper.lerp(t, startingPos.x, controlPoint.x);
		double lerpA1Y = MathHelper.lerp(t, startingPos.y, controlPoint.y);
		double lerpA1Z = MathHelper.lerp(t, startingPos.z, controlPoint.z);

		double lerpA2X = MathHelper.lerp(t, controlPoint.x, controlPoint2.x);
		double lerpA2Y = MathHelper.lerp(t, controlPoint.y, controlPoint2.y);
		double lerpA2Z = MathHelper.lerp(t, controlPoint.z, controlPoint2.z);

		double lerpA3X = MathHelper.lerp(t, controlPoint2.x, endingPos.x);
		double lerpA3Y = MathHelper.lerp(t, controlPoint2.y, endingPos.y);
		double lerpA3Z = MathHelper.lerp(t, controlPoint2.z, endingPos.z);

		double lerpB1X = MathHelper.lerp(t, lerpA1X, lerpA2X);
		double lerpB1Y = MathHelper.lerp(t, lerpA1Y, lerpA2Y);
		double lerpB1Z = MathHelper.lerp(t, lerpA1Z, lerpA2Z);

		double lerpB2X = MathHelper.lerp(t, lerpA2X, lerpA3X);
		double lerpB2Y = MathHelper.lerp(t, lerpA2Y, lerpA3Y);
		double lerpB2Z = MathHelper.lerp(t, lerpA2Z, lerpA3Z);

		double lerpFinalX = MathHelper.lerp(t, lerpB1X, lerpB2X);
		double lerpFinalY = MathHelper.lerp(t, lerpB1Y, lerpB2Y);
		double lerpFinalZ = MathHelper.lerp(t, lerpB1Z, lerpB2Z);

		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.x = lerpFinalX;
		this.y = lerpFinalY;
		this.z = lerpFinalZ;

		PlayerEntity player = this.level.getNearestPlayer(this.x, this.y, this.z, 10.0D, false);
		if (player != null) {
			if (player.getMainHandItem().getItem() == ItemRegistry.GRIM_SCYTHE.get()) {
				endingPos = player.getPosition(1).add(0, player.getEyeHeight() - 0.25, 0);
			}
		}

		if (age++ >= lifetime) {
			remove();
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite sprites;

		public Factory(IAnimatedSprite sprite) {
			sprites = sprite;
		}

		public Particle createParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double x1, double y1, double z1) {
			LifestealParticle p = new LifestealParticle(world, x, y, z, x1, y1, z1);
			p.setSpriteFromAge(sprites);
			return p;
		}
	}
}
