package com.steelworks.Network;

import com.steelworks.Steelworks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Supplier;

public class ParticleClientMessageHandler {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void onMessageReceived(final ParticleMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
		NetworkEvent.Context ctx = ctxSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);
		if (sideReceived != LogicalSide.CLIENT) {
			Steelworks.LOGGER.warn("ParticleMessage received on wrong side:" + ctx.getDirection().getReceptionSide());
			return;
		}
		if (!message.initialized) {
			Steelworks.LOGGER.warn("ParticleMessage was invalid: " + message);
			return;
		}

		Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
		if (!clientWorld.isPresent()) {
			Steelworks.LOGGER.warn("ParticleMessage context could not provide a ClientWorld.");
			return;
		}
		ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
	}

	private static void processMessage(ClientWorld worldClient, ParticleMessage message) {
		for (int i = 0; i < message.a; i++) {
			worldClient.addParticle((IParticleData) ForgeRegistries.PARTICLE_TYPES.getValue(message.particle), message.x, message.y, message.z, message.mx, message.my, message.mz);
		}
	}
}
