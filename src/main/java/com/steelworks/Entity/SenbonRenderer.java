package com.steelworks.Entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steelworks.Steelworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SenbonRenderer extends EntityRenderer<SenbonEntity> {

	public SenbonRenderer(EntityRendererManager manager) {
		super(manager);
	}

	public void render(SenbonEntity entity, float f1, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int lighting) {
		if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
			matrix.pushPose();

			matrix.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.yRotO, entity.yRot) - 90F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.xRotO, entity.xRot) + 225F));

			Steelworks.LOGGER.info(partialTicks);
			Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemCameraTransforms.TransformType.FIXED, lighting, OverlayTexture.NO_OVERLAY, matrix, buffer);
			matrix.popPose();
			super.render(entity, f1, partialTicks, matrix, buffer, lighting);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(SenbonEntity entity) {
		return AtlasTexture.LOCATION_BLOCKS;
	}
}
