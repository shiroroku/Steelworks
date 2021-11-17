package com.steelworks.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steelworks.Configuration;
import com.steelworks.Network.BleedUpdateMessage;
import com.steelworks.Steelworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class BleedHUDRenderer {

	private static final int maxStacks = 10;
	private static final ResourceLocation empty = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed/empty.png");
	private static final ResourceLocation fill = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed/fill.png");
	private static final ResourceLocation flash = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed/flash.png");
	private static final ResourceLocation darken = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed/darken.png");

	private static int borderFlashTimer = 0;
	private static int fillFlashTimer = 0;
	public static int ClientBleedstacks = 0;
	public static boolean doBorderFlash = false;
	public static boolean doFillFlash = false;

	public static void recieveUpdate(ClientWorld worldClient, BleedUpdateMessage message) {
		if (ClientBleedstacks < message.stacks) {
			doBorderFlash = true;
		} else {
			if (doBorderFlash) {
				doBorderFlash = false;
			}
		}

		if (ClientBleedstacks > message.stacks) {
			doFillFlash = true;
		} else {
			if (doFillFlash) {
				doFillFlash = false;
			}
		}

		ClientBleedstacks = message.stacks;

	}

	@OnlyIn(Dist.CLIENT)
	public static void renderBleedHUD(RenderGameOverlayEvent.Post e) {
		Minecraft client = Minecraft.getInstance();

		if (ClientBleedstacks > 0) {
			MatrixStack stack = e.getMatrixStack();

			int width = 9;
			int height = 10;
			int y = client.getWindow().getGuiScaledHeight() - 75;
			int x = client.getWindow().getGuiScaledWidth() / 2 - ((BleedHUDRenderer.maxStacks * (width + Configuration.BLEED_XSPACING.get())) / 2);

			if (doBorderFlash) {
				if ((client.player.tickCount / 4) % 2 == 0) {
					client.getTextureManager().bind(BleedHUDRenderer.flash);
				} else {
					client.getTextureManager().bind(BleedHUDRenderer.empty);
				}
				borderFlashTimer++;

				if (borderFlashTimer >= 2000) {
					doBorderFlash = false;
					borderFlashTimer = 0;
				}
			} else {
				client.getTextureManager().bind(BleedHUDRenderer.empty);
			}

			for (int i = 0; i < BleedHUDRenderer.maxStacks; i++) {
				AbstractGui.blit(stack, x + (i * (width + Configuration.BLEED_XSPACING.get())) + Configuration.BLEED_XOFFSET.get(), y + Configuration.BLEED_YOFFSET.get(), 0, 0, width, height, width, height);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

			if (doFillFlash && !doBorderFlash) {
				if ((client.player.tickCount / 4) % 2 == 0) {
					client.getTextureManager().bind(BleedHUDRenderer.darken);
				} else {
					client.getTextureManager().bind(BleedHUDRenderer.fill);
				}
				fillFlashTimer++;

				if (fillFlashTimer >= 2000) {
					doFillFlash = false;
					fillFlashTimer = 0;
				}
			} else {
				client.getTextureManager().bind(BleedHUDRenderer.fill);
			}
			for (int i = 0; i < ClientBleedstacks; i++) {
				AbstractGui.blit(stack, x + (i * (width + Configuration.BLEED_XSPACING.get())) + Configuration.BLEED_XOFFSET.get(), y + Configuration.BLEED_YOFFSET.get(), 0, 0, width, height, width, height);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
		}
	}
}
