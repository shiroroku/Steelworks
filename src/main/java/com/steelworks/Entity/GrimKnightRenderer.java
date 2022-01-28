package com.steelworks.Entity;

import com.steelworks.Registry.EntityRegistry;
import com.steelworks.Steelworks;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class GrimKnightRenderer extends BipedRenderer<GrimKnightEntity, GrimKnightModel> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Steelworks.MODID, "textures/entity/" + EntityRegistry.ID_GRIM_KNIGHT + ".png");

	public GrimKnightRenderer(EntityRendererManager manager) {
		super(manager, new GrimKnightModel(), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(GrimKnightEntity entity) {
		return TEXTURE;
	}
}