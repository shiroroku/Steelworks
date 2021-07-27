package com.steelworks;

import com.steelworks.Data.DataConfigJsonReloader;
import com.steelworks.Effect.RageEffect;
import com.steelworks.Item.GrimScythe;
import com.steelworks.Item.SteelScythe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class Events {
	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(Events::onLivingDamage);
		MinecraftForge.EVENT_BUS.addListener(Events::onLivingDeath);
		MinecraftForge.EVENT_BUS.addListener(Events::onPlayerAttack);
		MinecraftForge.EVENT_BUS.addListener(Events::onAddReloadListenerEvent);
	}

	public static void onLivingDamage(LivingDamageEvent e) {
		RageEffect.handleLivingDamage(e);
	}

	public static void onLivingDeath(LivingDeathEvent e) {
		GrimScythe.handleLivingDeath(e);
	}

	public static void onPlayerAttack(AttackEntityEvent e) {
		SteelScythe.handlePlayerAttack(e);
	}

	public static void onAddReloadListenerEvent(AddReloadListenerEvent e) {
		e.addListener(new DataConfigJsonReloader());
	}
}
