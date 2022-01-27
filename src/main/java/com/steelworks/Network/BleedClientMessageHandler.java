package com.steelworks.Network;

import com.steelworks.Capability.Bleed;
import com.steelworks.Capability.BleedCapability;
import com.steelworks.CommonSetup;
import com.steelworks.Render.BleedHUDRenderer;
import com.steelworks.Steelworks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Optional;
import java.util.function.Supplier;

public class BleedClientMessageHandler {

	public static void onMessageReceived(final BleedUpdateMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (sideReceived != LogicalSide.CLIENT) {
			Steelworks.LOGGER.warn("BleedUpdateMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
		}
		if (!message.initialized) {
			Steelworks.LOGGER.warn("BleedUpdateMessage was invalid: " + message);
			return;
		}

		Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
		if (!clientWorld.isPresent()) {
			Steelworks.LOGGER.warn("BleedUpdateMessage context could not provide a ClientWorld.");
			return;
		}
		ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
	}

	private static void processMessage(ClientWorld worldClient, BleedUpdateMessage message) {
		BleedHUDRenderer.recieveUpdate(worldClient, message);
	}

	/***
	 * Syncs bleed to the client when they login.
	 */
	public static void handleOnPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
		if (!e.getPlayer().level.isClientSide) {
			Bleed player_bleed = e.getEntityLiving().getCapability(BleedCapability.CAPABILITY).orElse(null);
			if (player_bleed != null) {
				CommonSetup.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) e.getPlayer()), new BleedUpdateMessage(player_bleed.getStacks()));
			}
		}
	}
}
