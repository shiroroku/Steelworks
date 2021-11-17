package com.steelworks.Network;

import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class BleedClientMessageHandler {

	private static final Logger LOGGER = LogManager.getLogger();

	public static int ClientBleedstacks = 0;

	public static void onMessageReceived(final BleedUpdateMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (sideReceived != LogicalSide.CLIENT) {
			LOGGER.warn("BleedUpdateMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
		}
		if (!message.initialized) {
			LOGGER.warn("BleedUpdateMessage was invalid: " + message);
			return;
		}

		Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
		if (!clientWorld.isPresent()) {
			LOGGER.warn("BleedUpdateMessage context could not provide a ClientWorld.");
			return;
		}
		ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
	}

	private static void processMessage(ClientWorld worldClient, BleedUpdateMessage message) {
		ClientBleedstacks = message.stacks;
	}
}
