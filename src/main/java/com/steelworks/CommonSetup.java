package com.steelworks;

import com.steelworks.Network.ClientMessageHandler;
import com.steelworks.Network.CustomParticleMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Steelworks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonSetup {

	public static SimpleChannel CHANNEL;
	public static final byte CUSTOMPARTICLE_MESSAGE_ID = 0;

	public static void init(final FMLCommonSetupEvent event) {
		CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Steelworks.MODID, "channel"), () -> "1.0", s -> true, s -> true);
		CHANNEL.registerMessage(CUSTOMPARTICLE_MESSAGE_ID, CustomParticleMessage.class, CustomParticleMessage::encode, CustomParticleMessage::decode, ClientMessageHandler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
}
