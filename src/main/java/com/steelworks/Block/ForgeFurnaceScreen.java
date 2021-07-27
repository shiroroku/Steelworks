package com.steelworks.Block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.steelworks.Steelworks;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ForgeFurnaceScreen extends ContainerScreen<ForgeFurnaceContainer> {

	public static final ResourceLocation GUI = new ResourceLocation(Steelworks.MODID, "textures/gui/forge_furnace.png");
	ForgeFurnaceContainer screenContainer;

	public ForgeFurnaceScreen(ForgeFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		this.screenContainer = screenContainer;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		super.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(GUI);
		int relX = (this.width - this.getXSize()) / 2;
		int relY = (this.height - this.getYSize()) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.getXSize(), this.getYSize());

		if (this.getMenu().getCookTime() != -1) {
			this.blit(matrixStack, relX + 83, relY + 36, 176, 0, 10, (int) ((1f - (float) this.getMenu().getCookTime() / (float) this.getMenu().getCookLength()) * 16));
		}
	}
}
