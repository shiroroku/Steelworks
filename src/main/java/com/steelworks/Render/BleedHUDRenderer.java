package com.steelworks.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steelworks.Network.BleedClientMessageHandler;
import com.steelworks.Steelworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class BleedHUDRenderer {

	private static final int maxStacks = 10;
	private static final ResourceLocation bleed_empty = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed_0.png");
	private static final ResourceLocation bleed_fill = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed_1.png");

	@OnlyIn(Dist.CLIENT)
	public static void renderBleedHUD(RenderGameOverlayEvent.Post e) {
		Minecraft client = Minecraft.getInstance();

		if (BleedClientMessageHandler.ClientBleedstacks > 0) {
			MatrixStack stack = e.getMatrixStack();

			int scale = 9;
			int y = client.getWindow().getGuiScaledHeight() - 75;
			int x = client.getWindow().getGuiScaledWidth() / 2 - ((BleedHUDRenderer.maxStacks * scale) / 2);

			client.getTextureManager().bind(BleedHUDRenderer.bleed_empty);
			for (int i = 0; i < BleedHUDRenderer.maxStacks; i++) {
				AbstractGui.blit(stack, x + (i * scale), y, 0, 0, scale, scale, scale, scale);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

			client.getTextureManager().bind(BleedHUDRenderer.bleed_fill);
			for (int i = 0; i < BleedClientMessageHandler.ClientBleedstacks; i++) {
				AbstractGui.blit(stack, x + (i * scale), y, 0, 0, scale, scale, scale, scale);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
		}
	}
}
