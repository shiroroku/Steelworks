package com.steelworks;

import com.steelworks.Capability.BleedCapability;
import com.steelworks.Capability.CapabilityAttacher;
import com.steelworks.Network.BleedClientMessageHandler;
import com.steelworks.Network.BleedUpdateMessage;
import com.steelworks.Network.ParticleMessage;
import com.steelworks.Network.ParticleClientMessageHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Steelworks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

	public static SimpleChannel CHANNEL;
	public static final byte CUSTOMPARTICLE_MESSAGE_ID = 0;
	public static final byte BLEEDUPDATE_MESSAGE_ID = 1;

	public static void init(final FMLCommonSetupEvent event) {
		CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Steelworks.MODID, "channel"), () -> "1.0", s -> true, s -> true);
		CHANNEL.registerMessage(CUSTOMPARTICLE_MESSAGE_ID, ParticleMessage.class, ParticleMessage::encode, ParticleMessage::decode, ParticleClientMessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(BLEEDUPDATE_MESSAGE_ID, BleedUpdateMessage.class, BleedUpdateMessage::encode, BleedUpdateMessage::decode, BleedClientMessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

		BleedCapability.register();
		MinecraftForge.EVENT_BUS.register(CapabilityAttacher.class);
	}
}
