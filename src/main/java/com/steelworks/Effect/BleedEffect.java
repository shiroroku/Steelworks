package com.steelworks.Effect;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.steelworks.Registry.EffectRegistry;
import com.steelworks.Steelworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.awt.*;

public class BleedEffect extends Effect {
	public BleedEffect() {
		super(EffectType.HARMFUL, 89747363);
	}

	private static final int maxStacks = 10;

	public static void handleLivingDamage(LivingDamageEvent e) {
		if (e.getSource().getEntity() != null) {
			if (e.getSource().getEntity() instanceof LivingEntity) {

				LivingEntity attacker = (LivingEntity) e.getSource().getEntity();
				ItemStack heldItem = attacker.getMainHandItem();
				if (heldItem.hasTag() && heldItem.getTag().contains("bleed_damage") && heldItem.getTag().getInt("bleed_damage") > 0) {
					int amt = heldItem.getTag().getInt("bleed_damage");
					EffectInstance bleedeffect = e.getEntityLiving().getEffect(EffectRegistry.BLEED.get());

					if (bleedeffect != null) {

						if (bleedeffect.getAmplifier() + amt >= maxStacks) {
							e.getEntityLiving().removeEffect(EffectRegistry.BLEED.get());
							e.getEntityLiving().hurt(DamageSource.GENERIC, e.getEntityLiving().getMaxHealth() * 0.25f);
						} else {
							e.getEntityLiving().addEffect(new EffectInstance(EffectRegistry.BLEED.get(), 100, bleedeffect.getAmplifier() + amt, false, true, false));
						}
					} else {
						e.getEntityLiving().addEffect(new EffectInstance(EffectRegistry.BLEED.get(), 100, amt, false, true, false));
					}
				}
			}
		}
	}

	public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager attr, int amp) {
		if (amp > 0 && amp < maxStacks) {
			entity.addEffect(new EffectInstance(EffectRegistry.BLEED.get(), 100, amp - 1, false, true, false));
		} else {
			super.removeAttributeModifiers(entity, attr, amp);
		}
	}

	public int getColor() {
		return Color.decode("0xff0000").getRGB();
	}

	private static final ResourceLocation bleed_empty = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed_0.png");
	private static final ResourceLocation bleed_fill = new ResourceLocation(Steelworks.MODID, "textures/gui/bleed_1.png");

	@OnlyIn(Dist.CLIENT)
	public static void renderBleedHUD(RenderGameOverlayEvent.Post e) {
		Minecraft client = Minecraft.getInstance();
		EffectInstance bleedeffect = client.player.getEffect(EffectRegistry.BLEED.get());
		if (bleedeffect != null) {
			MatrixStack stack = e.getMatrixStack();

			int scale = 9;
			int y = client.getWindow().getGuiScaledHeight() - 75;
			int x = client.getWindow().getGuiScaledWidth() / 2 - ((maxStacks * scale) / 2);

			client.getTextureManager().bind(bleed_empty);
			for (int i = 0; i < maxStacks; i++) {
				AbstractGui.blit(stack, x + (i * scale), y, 0, 0, scale, scale, scale, scale);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

			client.getTextureManager().bind(bleed_fill);
			for (int i = 0; i < bleedeffect.getAmplifier(); i++) {
				AbstractGui.blit(stack, x + (i * scale), y, 0, 0, scale, scale, scale, scale);
			}
			client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
		}
	}
}
