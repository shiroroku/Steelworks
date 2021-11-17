package com.steelworks;

import com.steelworks.Capability.Bleed;
import com.steelworks.Data.DataConfigJsonReloader;
import com.steelworks.Effect.RageEffect;
import com.steelworks.Item.GrimScythe;
import com.steelworks.Item.SteelScythe;
import com.steelworks.Network.BleedClientMessageHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class Events {
	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(Events::onLivingDamage);
		MinecraftForge.EVENT_BUS.addListener(Events::onLivingDeath);
		MinecraftForge.EVENT_BUS.addListener(Events::onLivingUpdate);
		MinecraftForge.EVENT_BUS.addListener(Events::onPlayerAttack);
		MinecraftForge.EVENT_BUS.addListener(Events::onPlayerLoggedIn);
		MinecraftForge.EVENT_BUS.addListener(Events::onAddReloadListenerEvent);
	}

	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
		BleedClientMessageHandler.handleOnPlayerLoggedIn(e);
	}

	public static void onLivingDamage(LivingDamageEvent e) {
		RageEffect.handleLivingDamage(e);
		Bleed.handleLivingDamage(e);
	}

	public static void onLivingDeath(LivingDeathEvent e) {
		GrimScythe.handleLivingDeath(e);
	}

	public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
		//Bleed.handleLivingUpdate(e);
	}

	public static void onPlayerAttack(AttackEntityEvent e) {
		SteelScythe.handlePlayerAttack(e);
	}

	public static void onAddReloadListenerEvent(AddReloadListenerEvent e) {
		e.addListener(new DataConfigJsonReloader());
	}
}
