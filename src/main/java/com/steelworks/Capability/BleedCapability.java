package com.steelworks.Capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class BleedCapability {
	@CapabilityInject(Bleed.class)
	public static Capability<Bleed> CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(Bleed.class, new Bleed.NBTStorage(), Bleed::create);
	}
}
