package com.steelworks.Capability;

import com.steelworks.Steelworks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityAttacher {
	@SubscribeEvent
	public static void attachCapabilityToEntityHandler(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (entity instanceof LivingEntity) {
			event.addCapability(new ResourceLocation(Steelworks.MODID, "capability_provider"), new CapabilityProvider());
		}
	}
}
