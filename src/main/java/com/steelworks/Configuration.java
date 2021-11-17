package com.steelworks;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {

	public static ForgeConfigSpec CLIENT_CONFIG;

	public static ForgeConfigSpec.IntValue BLEED_XOFFSET;
	public static ForgeConfigSpec.IntValue BLEED_YOFFSET;
	public static ForgeConfigSpec.IntValue BLEED_XSPACING;

	static {

		ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
		clientBuilder.comment("Bleed hud config").push("Bleed HUD");
		BLEED_XOFFSET = clientBuilder.comment("X offset of the bleed bar.").defineInRange("offset_x", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		BLEED_YOFFSET = clientBuilder.comment("Y offset of the bleed bar.").defineInRange("offset_y", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		BLEED_XSPACING = clientBuilder.comment("X spacing between bleed drops.").defineInRange("spacing_x", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		clientBuilder.pop();

		CLIENT_CONFIG = clientBuilder.build();
	}
}
