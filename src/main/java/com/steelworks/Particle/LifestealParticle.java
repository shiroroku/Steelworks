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
	public void tick() {
		float t = (float) this.age / this.lifetime;

		Vector3d lerpA1 = lerpVector(t, startingPos, controlPoint);
		Vector3d lerpA2 = lerpVector(t, controlPoint, controlPoint2);
		Vector3d lerpA3 = lerpVector(t, controlPoint2, endingPos);
		Vector3d lerpB1 = lerpVector(t, lerpA1, lerpA2);
		Vector3d lerpB2 = lerpVector(t, lerpA2, lerpA3);
		Vector3d lerpFinal = lerpVector(t, lerpB1, lerpB2);

		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		this.x = lerpFinal.x;
		this.y = lerpFinal.y;
		this.z = lerpFinal.z;

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

	private static Vector3d lerpVector(double percent, Vector3d a, Vector3d b) {
		return new Vector3d(MathHelper.lerp(percent, a.x, b.x), MathHelper.lerp(percent, a.y, b.y), MathHelper.lerp(percent, a.z, b.z));
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
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
