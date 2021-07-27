package com.steelworks;

import com.steelworks.Item.SteelWrench;
import com.steelworks.Particle.LifestealParticle;
import com.steelworks.Registry.ItemRegistry;
import com.steelworks.Registry.ParticleRegistry;
import com.steelworks.Registry.ScreenRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Steelworks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.addListener(ClientSetup::onBlockOutline);
		ScreenRegistry.init();
		event.enqueueWork(() -> ItemModelsProperties.register(ItemRegistry.STEEL_SWORD.get(), new ResourceLocation(Steelworks.MODID, "blocking"), (stack, world, living) -> living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F));
	}

	public static void onBlockOutline(DrawHighlightEvent.HighlightBlock e) {
		SteelWrench.handleOnBlockOutline(e);
	}

	@SubscribeEvent
	public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent e) {
		Minecraft.getInstance().particleEngine.register(ParticleRegistry.LIFESTEAL.get(), LifestealParticle.Factory::new);
	}
}