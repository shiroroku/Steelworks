package com.steelworks.Effect;

import com.steelworks.Item.GrimArmor;
import com.steelworks.Registry.EffectRegistry;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import java.awt.*;

public class RageEffect extends Effect {
	public RageEffect() {
		super(EffectType.NEUTRAL, 89747363);
	}

	public static void handleLivingDamage(LivingDamageEvent e) {
		if (e.getEntityLiving() instanceof PlayerEntity && e.getSource().getEntity() != null) {
			PlayerEntity player = (PlayerEntity) e.getEntityLiving();
			for (ItemStack armorPiece : player.getArmorSlots()) {
				if (!(armorPiece.getItem() instanceof GrimArmor)) {
					return;
				}
			}

			float health = Math.max(player.getHealth() - e.getAmount(), 0);
			if (health == 0) {
				return;
			}

			float percentageLeft = health / player.getMaxHealth();
			if (percentageLeft < 0.75f) {
				if (percentageLeft < 0.50f) {
					if (percentageLeft < 0.25f) {
						player.addEffect(new EffectInstance(EffectRegistry.RAGE.get(), 4 * 20, 3, false, false, true));
						return;
					}
					player.addEffect(new EffectInstance(EffectRegistry.RAGE.get(), 6 * 20, 2, false, false, true));
					return;
				}
				player.addEffect(new EffectInstance(EffectRegistry.RAGE.get(), 8 * 20, 1, false, false, true));
			}
		}
	}

	public double getAttributeModifierValue(int strength, AttributeModifier attribute) {
		return strength * 2f;
	}

	public int getColor() {
		return Color.decode("0xff0000").getRGB();
	}

}
