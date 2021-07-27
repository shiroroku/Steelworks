package com.steelworks.Registry;

import com.steelworks.Block.ForgeFurnaceScreen;
import net.minecraft.client.gui.ScreenManager;

public class ScreenRegistry {

	public static void init() {
		ScreenManager.register(ContainerRegistry.FORGE_FURNACE.get(), ForgeFurnaceScreen::new);
	}
}
