package com.steelworks;

import net.minecraftforge.common.ForgeConfigSpec;

public class Configuration {

	public static ForgeConfigSpec CLIENT_CONFIG;
	public static ForgeConfigSpec.IntValue BLEED_XOFFSET;
	public static ForgeConfigSpec.IntValue BLEED_YOFFSET;
	public static ForgeConfigSpec.IntValue BLEED_XSPACING;

	public static ForgeConfigSpec COMMON_CONFIG;
	public static ForgeConfigSpec.IntValue BLEED_DECAYINTERVAL;
	public static ForgeConfigSpec.DoubleValue BLEED_DAMAGE;

	static {

		ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
		clientBuilder.comment("Bleed hud config").push("Bleed HUD");
		BLEED_XOFFSET = clientBuilder.comment("X offset of the bleed bar.").defineInRange("offset_x", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		BLEED_YOFFSET = clientBuilder.comment("Y offset of the bleed bar.").defineInRange("offset_y", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		BLEED_XSPACING = clientBuilder.comment("X spacing between bleed drops.").defineInRange("spacing_x", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
		clientBuilder.pop();
		CLIENT_CONFIG = clientBuilder.build();

		ForgeConfigSpec.Builder commonBulder = new ForgeConfigSpec.Builder();
		commonBulder.comment("Bleed damage config").push("Bleed");
		BLEED_DECAYINTERVAL = commonBulder.comment("The interval in seconds that bleed should decay.").defineInRange("decay_interval", 4, 0, Integer.MAX_VALUE);
		BLEED_DAMAGE = commonBulder.comment("Percentage of damage (of max health) bleed does.").defineInRange("damage", 0.2D, 0D, 1D);
		commonBulder.pop();
		COMMON_CONFIG = commonBulder.build();

	}
}
